import sbt._

object Dependencies {

  object Versions {
    val cats = "2.1.1"
    val catsEffect = "2.1.2"
    val catsMeowMtl = "0.4.0"
    val console4cats = "0.8.1"
    val derevo = "0.11.0"
    val fs2 = "2.3.0"
    val logback = "1.2.1"
    val monocle = "2.0.4"
    val newtype = "0.4.3"
    val refined = "0.9.13"

    val betterMonadicFor = "0.3.1"
    val contextApplied = "0.1.2"
    val kindProjector = "0.11.0"
    val skunk = "0.0.7"
    val http4s = "0.21.2"
    val circe = "0.13.0"
    val http4sJwtAuth = "0.0.4"
    val log4cats = "1.0.1"
    val catsRetry = "1.1.0"

  }

  object Libraries {
    val cats = "org.typelevel" %% "cats-core" % Versions.cats
    val catsEffect = "org.typelevel" %% "cats-effect" % Versions.catsEffect
    val console4cats = "dev.profunktor" %% "console4cats" % Versions.console4cats
    val catsRetry = "com.github.cb372" %% "cats-retry" % Versions.catsRetry

    val fs2 = "co.fs2" %% "fs2-core" % Versions.fs2
    def http4s(artifact: String): ModuleID = "org.http4s" %% artifact % Versions.http4s
    def circe(artifact: String): ModuleID = "io.circe" %% artifact % Versions.circe

    val circeCore = circe("circe-core")
    val circeGeneric = circe("circe-generic")
    val circeParser = circe("circe-parser")
    val circeRefined = circe("circe-refined")

    val http4sDsl = http4s("http4s-dsl")
    val http4sServer = http4s("http4s-blaze-server")
    val http4sClient = http4s("http4s-blaze-client")
    val http4sCirce = http4s("http4s-circe")

    val http4sJwtAuth = "dev.profunktor" %% "http4s-jwt-auth" % Versions.http4sJwtAuth

    val catsMeowMtlCore = "com.olegpy" %% "meow-mtl-core" % Versions.catsMeowMtl
    val catsMeowMtlEffects = "com.olegpy" %% "meow-mtl-effects" % Versions.catsMeowMtl

    val derevoCats = "org.manatki" %% "derevo-cats" % Versions.derevo
    val derevoTagless = "org.manatki" %% "derevo-cats-tagless" % Versions.derevo

    val refinedCore = "eu.timepit" %% "refined" % Versions.refined
    val refinedCats = "eu.timepit" %% "refined-cats" % Versions.refined

    val log4cats = "io.chrisdavenport" %% "log4cats-slf4j" % Versions.log4cats
    val newtype = "io.estatico" %% "newtype" % Versions.newtype

    val monocleCore = "com.github.julien-truffaut" %% "monocle-core" % Versions.monocle
    val monocleMacro = "com.github.julien-truffaut" %% "monocle-macro" % Versions.monocle

    val skunk = "org.tpolecat" %% "skunk-core" % Versions.skunk
    // Runtime
    val logback = "ch.qos.logback" % "logback-classic" % Versions.logback
  }

  object CompilerPlugins {
    val betterMonadicFor = compilerPlugin("com.olegpy" %% "better-monadic-for" % Versions.betterMonadicFor)
    val contextApplied = compilerPlugin("org.augustjune" %% "context-applied" % Versions.contextApplied)
    val kindProjector = compilerPlugin(
      ("org.typelevel" %% "kind-projector" % Versions.kindProjector).cross(CrossVersion.full)
    )
  }

}