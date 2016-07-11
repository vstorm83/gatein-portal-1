(function($) {
	var data = [{'id': '1', 'name': 'abc', 'icon': 'plus'},
	            {'id': '2', 'name': 'def', 'icon': 'plus'},
				{'id': '3', 'name': 'ghi', 'icon': 'plus'}];
	
	var sample = {
		init : function() {
			$('#customRenderMentions').commonMentions({
				triggerChar: '',	
				allowAdd: true,
				val: function(msg) {
					if (msg) {
						console.log(msg);
						$('.tagList').append(msg);
						$(this).html('');
					} else {
						return "";
					}
				},
				onAutoCompleteItemClick: function(id) {
					var results = [];
					$.each(data, function(idx, elm) {
						if (elm.id == id) {
							results.push(elm);
						} 
					});
					if (results.length) {
						return results[0];
					} else {
						return {'id': id, 'name': id, 'icon': 'plus'};
					}
				},
				addMessageMenu : function(parent, msg, allowAdd) {
					if (allowAdd) {
						$('<li class="data" data-uid="'+ msg +'"></li>')
						.html('adding <em>'+msg+'...</em>')
						.appendTo(parent);						
					}
				},
				onDataRequest: function(action, query, callback) {
					query = '[' + query.split('').join(',') + ']';
					query = new RegExp(query);
					
					var results = [];
					$.each(data, function(idx, elm) {
						if (query.test(elm.name)) {
							results.push(elm);
						} 
					});
					callback.call(this, results);
				}
			});
		}
	};
	return sample;
})($);