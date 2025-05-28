$(document).ready(function() {
	console.log('Document ready');

	// Handle pagination with AJAX
	$(document).on('click', '.page', function(event) {
		event.preventDefault();

		var page = $(this).data('page');
		var entity = $(this).data('entity') || 'users';
		var container = $(this).data('target') || '#content';
		var sortField = $('.sort.active').data('field') || 'userId';
		var sortDir = $('.sort.active').data('dir') || 'asc';
		var search = encodeURIComponent($('input[name="search"]').val() || '');

		var url = '/' + entity + '/html?page=' + page + '&sortField=' + sortField + '&sortDir=' + sortDir + '&search=' + search;
		loadContent(url, container);
	});

	// Handle sort functionality with AJAX
	/*$(document).on('click', '.sort', function (event) {
		event.preventDefault();
		var sortField = $(this).data('field');
		sortEntities(sortField);
	});*/

	// Handle search input change with AJAX
	/*$(document).on('input', 'input[name="search"]', function () {
		searchEntities('/users/html', '#content', 'userId');
	});*/

	// Handle AJAX link clicks
	$(document).on('click', '.ajax-link', function(event) {
	    event.preventDefault();
	    
	    // Remove 'dropbtn' class from all other menu items first
	    $('.menu.dropbtn').removeClass('dropbtn');
	    
	    // Check if clicked element is a menu item
	    var $clickedLink = $(this);
	    if ($clickedLink.hasClass('menu')) {
	        $clickedLink.addClass('dropbtn'); // Add only to current clicked menu
	    }
	    
	    // Proceed with AJAX load
	    loadContent($clickedLink.attr('href'), '#content');
	});
	// Form submission with AJAX
	window.submitHtmlForm = function(formId) {
		console.log('Button clicked');

		$('#error-messages').empty(); // Clear error messages
		$('.error-message').html('');
		$('input').removeClass('has-error');

		var form = $('#' + formId);
		if (form.length === 0) {
			console.error('Form not found');
			return;
		}

		var formData = form.serialize();
		var actionUrl = form.attr('action');

		if ($('#ref_id', form).length > 0) {
			var refId = $('#ref_id', form).val();
			if (!refId) {
				console.error('Reference ID not found');
				return;
			}
			actionUrl += '/' + refId;
		}

		console.log('Form data:', formData);

		$.ajax({
			type: 'POST',
			url: actionUrl,
			data: formData,
			dataType: 'json',
			contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
			success: function(response) {
				if (response.status === 'error') {
					handleFormErrors(response);
				} else if (response.status === 'success') {
					$('#dynamicModal').modal('hide');
					$('#nestedModal').modal('hide');
					$('<div>' + response.message + '</div>').dialog({
						title: 'Success',
						modal: true,
						buttons: {
							Ok: function() {
								$(this).dialog('close');
								if (response.loadnext) {

									if (response.loadnext == 'reload') {
										window.location.reload();
										return;
									}

									else if (response.target == 'modal') {
										loadContent(response.loadnext, response.target);
									}

									else {

										loadContent(response.loadnext, ('#' + (response.target || 'content')));
										form.parent().html('');
									}
								}
							}
						}
					});
				}
			},
			error: function(xhr, status, error) {
				//console.error('Error Status:', status);
				//console.error('Error Message:', error);

				try {
					var response = JSON.parse(xhr.responseText);
					handleFormErrors(response);
				} catch (e) {
					$('#error-messages').html('<p class="error-message">Failed to process form. Please try again.</p>');
				}
			}
		});
	};

	function handleFormErrors(response) {
		// Clear previous error messages
		$('#error-messages').empty(); // Clear error messages container
		$('.error-message').remove(); // Remove all existing error messages
		$('input, select').removeClass('has-error'); // Remove error classes

		// Display the general error message
		if (response.message) {
			$('#error-messages').append('<p class="error-message" style="color:red;">' + response.message + '</p>');
		}

		// Iterate through each error in the response
		$.each(response.errors, function(field, error) {
			// Normalize the field name to match the input names in the form
			var fieldIndex = field.match(/\d+/); // Extract the index (e.g., 0 or 1)
			var normalizedFieldName = field.replace(/([A-Z])/g, '_$1').toLowerCase().replace(/\[\d+\]/, '[]'); // Normalize to snake_case

			// Handle array-type fields
			if (fieldIndex) {
				// Construct the selector for the array fields
				var fieldElement = $('[name="' + normalizedFieldName + '"]').eq(fieldIndex[0]);

				if (fieldElement.length > 0) {
					// Highlight the field and display the error message
					fieldElement.addClass('has-error');
					fieldElement.after('<span class="error-message" style="color:red;">' + error + '</span>');
				} else {
					// If the field is not found, append the error message to the container
					$('#error-messages').append('<p class="error-message" style="color:red;">' + error + '</p>');
				}
			} else {
				// Handle regular fields (without array notation)
				var fieldElement = $('[name="' + field + '"]');

				if (fieldElement.length > 0) {
					// Highlight the field and display the error message
					fieldElement.addClass('has-error');
					fieldElement.after('<span class="error-message" style="color:red;">' + error + '</span>');
				} else {
					// If the field is not found, append the error message to the container
					$('#error-messages').append('<p class="error-message" style="color:red;">' + error + '</p>');
				}
			}
		});
	}








	// Clear error messages when interacting with form fields
	$(document).on('focus', 'input', function() {
		$(this).removeClass('has-error');
		$(this).next('.error-message').remove();
	});

	// Status toggle function
	$(document).on('click', '.status-toggle', function() {
		var refId = $(this).data('id');
		var url = $(this).data('url');
		var currentStatus = $(this).text().trim();
		var newStatus = currentStatus === "Enable" ? 1 : 2;
		var element = $(this);

		$.ajax({
			type: 'POST',
			url: url,
			data: JSON.stringify({ userId: refId, userStatus: newStatus }),
			contentType: 'application/json',
			success: function(response) {
				if (response.status === 'success') {
					element.html(response.active === 'Y' ? "Disable" : "Enable");
					$('#flag_' + refId).toggleClass('active_green inactive_red');
				}
			},
			error: function(error) {
				console.error("Error toggling status:", error);
			}
		});
	});
});

// Search functionality
function searchEntities(listingUrl, targetContainer, defaultSortField) {
	var search = $('input[name="search"]').val();
	var sortField = $('.sort.active').data('field') || defaultSortField;
	var sortDir = $('.sort.active').data('dir') || 'asc';
	var page = $('.pagination .active').find('a').data('page') || 0;

	var url = listingUrl + '?page=' + page + '&sortField=' + sortField + '&sortDir=' + sortDir + '&search=' + encodeURIComponent(search);

	loadContent(url, targetContainer);  // Use loadContent to handle AJAX
}


function filterReports(listingUrl, targetContainer, defaultSortField) {
	// Serialize all form inputs into a query string
	var formData = $('#search-form').serialize(); // Automatically grabs all input fields in the form

	// Get sorting and pagination data
	var sortField = $('.sort.active').data('field') || defaultSortField;
	var sortDir = $('.sort.active').data('dir') || 'asc';
	var page = $('.pagination .active').find('a').data('page') || 0;

	// Construct the URL with query parameters
	var url = listingUrl + '?page=' + page
		+ '&sortField=' + sortField
		+ '&sortDir=' + sortDir
		+ '&' + formData; // Append form data (all inputs from the form)

	// Perform AJAX request and load content into the target container
	loadContent(url, targetContainer);
}

function sortEntities(sortField) {
	var targetContainer = '#content'; // Container where the content will be loaded
	var defaultSortField = 'userId'; // Default sort field

	var sortDir = $('.sort[data-field="' + sortField + '"]').data('dir') === 'asc' ? 'desc' : 'asc';  // Toggle sort direction

	// Update the sorting state in the DOM
	$('.sort').removeClass('active').data('dir', 'asc'); // Reset all to 'asc'
	$('.sort[data-field="' + sortField + '"]').addClass('active').data('dir', sortDir);

	// Trigger the search with updated sort direction and field
	searchEntities(listingUrl, targetContainer, defaultSortField);
}


function loadContent(url, targetSelector) {

	console.log(targetSelector);

	const $target = $(targetSelector);
	const isModal = $target.is('.modal') || targetSelector === 'modal' || $target.is('.modal2') || targetSelector === 'modal2';

	$.ajax({
		url: encodeURI(url),
		method: 'GET',

		success(data) {

			if (data.status === 'success') {
				$('#dynamicModal').modal('hide');
				$('<div>' + data.message + '</div>').dialog({
					title: 'Success',
					modal: true,
					buttons: {
						Ok: function() {
							$(this).dialog('close');
							if (data.loadnext) {

								if (data.loadnext == 'reload') {
									window.location.reload();
									return;
								}
								else if (data.target == 'modal') {
									loadContent(data.loadnext, data.target);
								}

								else {

									loadContent(data.loadnext, ('#' + (data.target || 'content')));
									form.parent().html('');
								}
							}
						}
					}
				});
			}



			else if (isModal) {
				// modal path

				const $modal = (targetSelector === 'modal') ? ($target.is('.modal') ? $target : $('#dynamicModal')) : $('#nestedModal')

				//const $modal = $target.is('.modal') ? $target : $('#dynamicModal');
				if (!$modal.length) {
					console.error('Modal not found:', targetSelector);
					return $target.html(data);
				}

				console.log(">>>>>>>>>>>>" + $modal);

				$modal.find('.modal-content').html(data);
				const bsModal = bootstrap.Modal.getInstance($modal[0]) || new bootstrap.Modal($modal[0]);
				bsModal.show();

				// focus first input
				setTimeout(() => {
					const $first = $modal.find('input,select,textarea').filter(':visible').first();
					if ($first.length) $first.focus();
				}, 300);
			} else {
				// regular pane

				console.log($target);


				$target.html(data);

				// scroll into view within nearest scrollable ancestor
				const $scrollable = $target.closest('.overflow-auto,body');

				/*$scrollable.animate({ 
				  scrollTop: $target.position().top + $scrollable.scrollTop() 
				}, 300);*/

				// outline flash
				/*$target.css('outline','2px solid var(--bs-success)');
				setTimeout(() => $target.css('outline',''), 500);*/

				// focus first input
				const $first = $target.find('input,select,textarea').filter(':visible').first();
				if ($first.length) $first.focus();
			}
		},

		/*error(xhr, status, err) {
		  const msg    = xhr.statusText || 'Server error';
		  const detail = xhr.responseText || '';
	
		  const errorHtml = `
			<div class="alert alert-danger" role="alert">
			  <h5>Failed to load content</h5>
			  <p>${msg}</p>
			  ${detail ? `<pre class="small text-muted mt-2" style="white-space:pre-wrap;">${detail}</pre>` : ''}
			</div>`;
	
		  if (isModal) {
			const $modal = $target.is('.modal') ? $target : $('#dynamicModal');
			if ($modal.length) {
			  $modal.find('.modal-body').html(errorHtml);
			  const bsModal = bootstrap.Modal.getInstance($modal[0]) || new bootstrap.Modal($modal[0]);
			  bsModal.show();
			} else {
			  console.error('Modal error fallback:', err);
			  $target.html(errorHtml);
			}
		  } else {
			$target.html(errorHtml);
		  }
	
		  console.error('loadContent error for', url, err);
		}*/
	});
}

function closeNestedModalOnly() {
	const nestedModal = bootstrap.Modal.getInstance('#nestedModal');
	$('#nestedModal').modal('hide');
}

