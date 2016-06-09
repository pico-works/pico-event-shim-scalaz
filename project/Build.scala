import sbt.Keys._
import sbt._

object Build extends sbt.Build {  
  val pico_atomic               = "org.pico"        %%  "pico-atomic"               % "0.0.1-2"
  val pico_disposal             = "org.pico"        %%  "pico-disposal"             % "0.0.1-2"
  val scalaz_7_2                = "org.scalaz"      %%  "scalaz-core"               % "7.2.3"
  val scalaz_7_1                = "org.scalaz"      %%  "scalaz-core"               % "7.1.8"

  val specs2_core               = "org.specs2"      %%  "specs2-core"               % "3.7.2"

  implicit class ProjectOps(self: Project) {
    def standard(theDescription: String) = {
      self
          .settings(scalacOptions in Test ++= Seq("-Yrangepos"))
          .settings(publishTo := Some("Releases" at "s3://dl.john-ky.io/maven/releases"))
          .settings(description := theDescription)
          .settings(isSnapshot := true)
    }

    def notPublished = self.settings(publish := {}).settings(publishArtifact := false)

    def libs(modules: ModuleID*) = self.settings(libraryDependencies ++= modules)

    def testLibs(modules: ModuleID*) = self.libs(modules.map(_ % "test"): _*)
  }

  lazy val `pico-event` = Project(id = "pico-event", base = file("pico-event"))
      .standard("Tiny publish-subscriber library")
      .libs(pico_atomic, pico_disposal)
      .testLibs(specs2_core)

  lazy val `pico-event-scalaz_7_2` = Project(id = "pico-event-scalaz_7_2", base = file("pico-event-scalaz_7_2"))
      .standard("Scalaz syntax support for pico-event")
      .dependsOn(`pico-event`)
      .libs(pico_atomic, pico_disposal)
      .libs(scalaz_7_2)
      .testLibs(specs2_core)
      .settings(unmanagedSourceDirectories in Compile += baseDirectory.value / "../pico-event-scalaz/src/main/scala")
      .settings(unmanagedSourceDirectories in Test    += baseDirectory.value / "../pico-event-scalaz/src/test/scala")

  lazy val `pico-event-scalaz_7_1` = Project(id = "pico-event-scalaz_7_1", base = file("pico-event-scalaz_7_1"))
      .standard("Scalaz syntax support for pico-event")
      .dependsOn(`pico-event`)
      .libs(pico_atomic, pico_disposal)
      .libs(scalaz_7_1)
      .testLibs(specs2_core)
      .settings(unmanagedSourceDirectories in Compile += baseDirectory.value / "../pico-event-scalaz/src/main/scala")
      .settings(unmanagedSourceDirectories in Test    += baseDirectory.value / "../pico-event-scalaz/src/test/scala")

  lazy val all = Project(id = "pico-event-project", base = file("."))
      .notPublished
      .aggregate(`pico-event`, `pico-event-scalaz_7_1`, `pico-event-scalaz_7_2`)
}
