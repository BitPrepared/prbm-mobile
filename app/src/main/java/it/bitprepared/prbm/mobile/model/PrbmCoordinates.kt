package it.bitprepared.prbm.mobile.model

data class PrbmCoordinates(
  var latitude: Double = 0.0,
  var longitude: Double = 0.0,
  var time: Long = 0,
  var bearing: Float = 0f,
  var speed: Float = 0f
)
