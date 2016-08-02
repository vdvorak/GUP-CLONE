'use strict'

const COLORS = {
    blue: "#1875D0",
    white: "white"
}

module.exports = function() {
  return {
    restrict: "E",
    require: '^ngModel',
    scope : {
      label: "@",
      ngModel: "=",
      color: "@",
      type: "@",
      validate: "=",
      isValid: "="
    },
    replace: true,
    template: `<div class="inputForm">
                 <label>{{ label }}</label>
                 <input type="{{ type || 'text'}}" ng-model="ngModel">
                 <div class="errors"></div>
               </div>`,
    controller: function($scope, $element, $timeout, $attrs) {
      let id = ee.on('form-submit', validate)
      $scope.$on("$destroy", function() {
        ee.off(id)
      }.bind(this))


      /* Update method from outside */
      document.addEventListener('update-text', function() {
        this.onFocus()
        this.onBlur()
      }.bind(this))


      let defaultBorder = ""

      let el = $element[0].getElementsByTagName('input')[0],
          label = $element[0].getElementsByTagName('label')[0],
          error = $element[0].getElementsByClassName('errors')[0]


      function validate() {
        if($scope.validate) {
          function handle(error) {
            if(typeof $scope.isValid !== "undefined") {
              if(error.length)
                $scope.isValid = false
              else
                $scope.isValid = true

              $timeout(function() {
                $scope.$apply()
              }.bind(this), 0)
            }
          }

          let resp = $scope.validate(el.value)

          if( typeof resp === "string")
            handle( error.innerHTML = resp)
          else
            resp.then( function(data) {
                error.innerHTML = data
                handle(data)
              }, console.error)


        }
      }

      this.onBlur = function(e) {
        if( !$scope.ngModel.length)
          hideAnimation()

        validate()
      }

      this.onFocus = function(e) {
        displayAnimation()
      }

      function displayAnimation() {
        label.style.color = COLORS[$scope.color]
        if(!defaultBorder.length) {
          defaultBorder = window.getComputedStyle(label.parentNode).borderBottom
        } else {
          label.parentNode.style.borderBottom = `2px solid ${COLORS[$scope.color]}`
        }

        label.classList.add('textOut')
      }

      function hideAnimation() {
        label.style.color = ""
        label.parentNode.style.borderBottom = defaultBorder
        label.classList.remove('textOut')
      }

      $timeout( () => {
        if( $scope.ngModel && $scope.ngModel.length )
          displayAnimation()
        else
          hideAnimation()
      }, 250)


      el.addEventListener('blur', this.onBlur.bind(this))
      el.addEventListener('focus', this.onFocus.bind($scope))
    }
  }
}
