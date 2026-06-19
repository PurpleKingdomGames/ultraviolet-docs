package examples

import indigo.*
import tyrian.*

import scala.scalajs.js.annotation.*

@JSExportTopLevel("IndigoGame")
object Runtime extends BasicGameRuntime[Unit]:

  def game: Game[?, ?, ?] =
    Stripes()

  def settings: Settings =
    Settings.default

  def eventMapping: PartialIso[GlobalMsg, GlobalEvent] =
    PartialIso.none

  def init(flags: Map[String, String]): Result[Unit] =
    Result(())

  def update(model: Unit): GlobalMsg => Result[Unit] =
    case _ => Result(model)
