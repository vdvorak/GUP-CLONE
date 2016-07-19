'use strict'

let utils = require('./utils'),
    config = require('../data/config')

let ctx = module.exports = {}

module.exports.init = function(data) {
  /* init data from database here */
  this.favourites = null
  this.mailbox = null
  this.user = null
  this.notifications = {hello : "preved"}

  ctx.checkUserIsLogged(function(err, data) {
    if(err) console.error(err)
    else {
      if(data) ctx.saveUserData(data)
      else console.log("User is not logged in")
    }
  }.bind(this))

  console.log("Database initialized")
}

module.exports.checkEmail = function(email, cb) {
  utils.request({
    "method" : config.routes.checkEmail.method,
    "url" : config.api.auth + config.routes.checkEmail.url,
    "data" : {
      "email" : email
    },
    "headers" : {
      "Content-Type" : "application/json"
    }
  }).then(data => cb(null, data), err => cb(err))
}

module.exports.login = function( data, cb ) {
  utils.request({
    "method" : config.routes.login.method,
    "url" : config.api.auth + config.routes.login.url,
    "data" : data,
    "headers" : {
      "Content-Type" : "application/json"
    }
  }).then(data => cb(null, data), err => cb(err))
}

/* This method does saves user data in this module only, no backend communication */
module.exports.saveUserData = function(data) {
  data = JSON.parse(data)
  this.user = {}
  /* TODO: распарсить данные в осмысленные переменные */

  console.log("Database:: User data saved successfully( шутка ) ")
}

module.exports.checkUserIsLogged = function( cb ) {
  utils.request({
    "method" : config.routes.checkLogged.method,
    "url" : config.api.url + config.routes.checkLogged.url
  }).then(data =>
    cb(null, data),
    err =>
    cb(err))
}
