'use strict'

module.exports = function($scope) {

  this.init = function() {
    this.showConversations = true
    this.showMessages = false
  }

  this.showMessage = id => {
    /* TODO: Insert exact dialog later */
    this.showMessages = true
    this.showConversations = false
  }

  this.back = function() {
    this.showMessages = false
    this.showConversations = true
  }

  this.showUserCabinet = id => {
    /* TODO redirect to user cabinet */
    console.log("Showing user cabinet. Soooon ;)")
  }
}
