import Dependencies._

name := "PracticalScalaFP"

version in ThisBuild := "0.0.1"

lazy val root = (project in file("."))
  .settings(
    name := "PracticalFPinScala",
    scalaVersion := "2.13.1",
    Global / onChangedBuildSource := ReloadOnSourceChanges,
    scalacOptions ++= CompilerOptions.cOptions,
    scalafmtOnCompile := true,
    libraryDependencies ++= rootDependencies
  )
  .configs(IntegrationTest)
  .settings(Defaults.itSettings)
