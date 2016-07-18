(function($) {
  var data = [
              {value: 'alex', uid: 'user:1'},
              {value: 'andrew', uid: 'user:2'},
              {value: 'angry birds', uid: 'game:5'},
              {value: 'assault', uid: 'game:3'}
          ];
  
  var sample = {
    init : function() {
      $('#tagMentions').mention({
        type : 0,
        source: function(query, callback) {
          query = '[' + query.split('').join(',') + ']';
          query = new RegExp(query);
          
          var results = [];
          $.each(data, function(idx, elm) {
            if (query.test(elm.value)) {
              results.push(elm);
            }
          });
          callback.call(this, results);
        },        
        renderMenuItem: function(item, escape) {
          console.log('rendering custom menu item');
          return $( "<li>" ).append( $( "<div>" ).text( item.value ) );
        }
//        renderItem: function(mention) {
//          console.log('rendering custom item');
//          var tpl = '<span data-mention="' + mention.uid + '">' + mention.label + 
//          '<i class="uiIconClose uiIconLightGray" onclick="this.parentNode.parentNode.removeChild(this.parentNode)"></i></span>';
//          return tpl;
//        }
      });
    }
  };
  return sample;
})($);