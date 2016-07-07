(function($) {
	var data = [{'id': '1', 'name': 'abc', 'icon': 'plus'},
	            {'id': '2', 'name': 'def', 'icon': 'plus'},
				{'id': '3', 'name': 'ghi', 'icon': 'plus'}];
	
	var sample = {
		init : function() {
			$('#sampleMentions').commonMentions({
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