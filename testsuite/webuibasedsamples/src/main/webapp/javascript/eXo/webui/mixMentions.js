(function($) {
  var data = [
              {value: 'alex', uid: 'user:1'},
              {value: 'andrew', uid: 'user:2'},
              {value: 'angry birds', uid: 'game:5'},
              {value: 'assault', uid: 'game:3'}
          ];
  
  var sample = {
    init : function() {
      $('#mixMentions').mention({
        type : 1,
        source: data,
        renderMenuItem: function($menu, item) {
          console.log('rendering custom menu item');
          return $( "<li>" ).append( $( "<div>" ).text( item.label ) ).appendTo( $menu );
        },
        renderItem: function(mention) {
          console.log('rendering custom item');
          var tpl = '<span data-mention="' + mention.uid + '">' + mention.label + 
          '<i class="uiIconClose uiIconLightGray" onclick="this.parentNode.parentNode.removeChild(this.parentNode)"></i></span>';
          return tpl;
        }
      });
    }
  };
  return sample;
})($);