import mill._
import mill.scalalib._
import mill.scalajslib._
import mill.scalajslib.api._

import $ivy.`io.indigoengine::mill-indigo:0.18.0`, millindigo._
import $ivy.`org.typelevel::scalac-options:0.1.7`, org.typelevel.scalacoptions._

trait ShaderModule extends MillIndigo {
  def scalaVersion   = "3.6.2"
  def scalaJSVersion = "1.18.1"

  override def scalacOptions = T {
    val flags = super.scalacOptions() ++
      ScalacOptions.defaultTokensForVersion(ScalaVersion.unsafeFromString(scalaVersion())) ++
      Seq("-Xfatal-warnings")

    /*
    By default, we get the following flags:

    -encoding, utf8, -deprecation, -feature, -unchecked,
    -language:experimental.macros, -language:higherKinds,
    -language:implicitConversions, -Xkind-projector,
    -Wvalue-discard, -Wnonunit-statement, -Wunused:implicits,
    -Wunused:explicits, -Wunused:imports, -Wunused:locals,
    -Wunused:params, -Wunused:privates, -Xfatal-warnings
     */

    // Alledgedly unused local definitions are unavoidable in Ultraviolet,
    // so we remove the flag to make things tolerable.
    flags.filterNot(_ == "-Wunused:locals")
  }

  def indigoOptions: IndigoOptions

  def makeIndigoOptions(title: String): IndigoOptions =
    IndigoOptions.defaults
      .withTitle(title)
      .withWindowSize(400, 400)
      .withAssetDirectory(os.RelPath.rel / "assets")
      .withBackgroundColor("black")
      .excludeAssets {
        case p if p.endsWith(os.RelPath.rel / ".gitkeep")  => true
        case p if p.endsWith(os.RelPath.rel / ".DS_Store") => true
        case _                                             => false
      }

  def indigoGenerators: IndigoGenerators =
    IndigoGenerators("generated")
      .generateConfig("Config", indigoOptions)
      .listAssets("Assets", indigoOptions.assets)

  val indigoVersion = "0.18.0"

  def ivyDeps =
    Agg(
      ivy"io.indigoengine::ultraviolet::0.4.0",
      ivy"io.indigoengine::indigo-json-circe::$indigoVersion",
      ivy"io.indigoengine::indigo::$indigoVersion",
      ivy"io.indigoengine::indigo-extras::$indigoVersion"
    )

  object test extends ScalaJSTests {
    def ivyDeps = Agg(
      ivy"org.scalameta::munit::1.0.4"
    )

    override def moduleKind = T(mill.scalajslib.api.ModuleKind.CommonJSModule)

    def testFramework: mill.T[String] = T("munit.Framework")
  }

}
