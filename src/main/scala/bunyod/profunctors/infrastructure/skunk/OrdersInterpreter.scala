package bunyod.profunctors.infrastructure.skunk

import bunyod.profunctors.domain.auth.AuthPayloads.UserId
import bunyod.profunctors.domain.cart.CartPayloads._
import bunyod.profunctors.domain.items.ItemsPayloads.ItemId
import bunyod.profunctors.domain.orders.OrdersPayloads._
import bunyod.profunctors.domain.orders._
import bunyod.profunctors.effects._
import bunyod.profunctors.extensions.Skunkx._
import bunyod.profunctors.routes.http.json._
import cats.effect._
import cats.implicits._
import skunk._
import skunk.codec.all._
import skunk.circe.codec.all._
import skunk.implicits._
import squants.market._

class OrdersInterpreter[F[_]: Sync: BracketThrow: GenUUID](
  sessionPool: Resource[F, Session[F]]
) extends OrdersAlgebra[F] {

  import OrdersInterpreter._

  override def get(userId: UserId, orderId: OrderId): F[Option[Order]] =
    sessionPool.use(session => session.prepare(selectByUserIdAndOrderId).use(q => q.option(userId ~ orderId)))

  override def findBy(userId: UserId): F[List[Order]] =
    sessionPool.use(session => session.prepare(selecByUserId).use(q => q.stream(userId, 1024).compile.toList))

  override def create(
    userId: UserId,
    paymentId: PaymentId,
    items: List[CartItem],
    total: Money
  ): F[OrderId] =
    sessionPool.use { session =>
      session.prepare(insertOrder).use { cmd =>
        GenUUID[F].make[OrderId].flatMap { id =>
          val itMap = items.map(x => x.item.uuid -> x.quantity).toMap
          val order = Order(id, paymentId, itMap, total)
          cmd.execute(userId ~ order).as(id)
        }
      }
    }

}

object OrdersInterpreter {

  private val decoder: Decoder[Order] = (uuid.cimap[OrderId] ~ uuid.cimap[UserId] ~ uuid.cimap[PaymentId] ~
    jsonb[Map[ItemId, Quantity]] ~ numeric.map[Money](USD.apply)).map {
    case o ~ _ ~ p ~ i ~ t =>
      Order(o, p, i, t)
  }

  val encoder: Encoder[UserId ~ Order] =
    (uuid.cimap[OrderId] ~ uuid.cimap[UserId] ~ uuid.cimap[PaymentId] ~
      jsonb[Map[ItemId, Quantity]] ~ numeric.contramap[Money](_.amount)).contramap {
      case id ~ o =>
        o.id ~ id ~ o.paymentId ~ o.items ~ o.total
    }

  val selecByUserId: Query[UserId, Order] =
    sql"""
         SELECT * FROM orders
         WHERE user_id == ${uuid.cimap[UserId]}
       """.query(decoder)

  val selectByUserIdAndOrderId: Query[UserId ~ OrderId, Order] =
    sql"""
         SELECT * FROM users
         WHERE user_id == ${uuid.cimap[UserId]}
         AND uuid == ${uuid.cimap[OrderId]}
       """.query(decoder)

  val insertOrder: Command[UserId ~ Order] =
    sql"""
         INSERT INTO orders
         VALUE ($encoder)
       """.command

}
