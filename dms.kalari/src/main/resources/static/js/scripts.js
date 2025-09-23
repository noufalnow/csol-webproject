$(document).ready(function() {
	console.log('Document ready');

	// Handle pagination with AJAX
	$(document).on('click', '.page', function(event) {
		event.preventDefault();

		var page = $(this).data('page');
		var entity = $(this).data('entity') || 'users';
		var container = $(this).data('target') || '#content';
		var sortField = $('.sort.active').data('field') || defaultSortField;
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
		searchEntities('/users', '#content', defaultSortField);
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

	    $('#error-messages').empty();
	    $('.error-message').html('');
	    $('input').removeClass('has-error');

	    var form = $('#' + formId);
	    if (form.length === 0) {
	        console.error('Form not found');
	        return;
	    }

	    //var hasFile = $('input[name="photoFileId"]', form).length > 0;
	    var dataToSend, contentType, processData;
		
		var formElement = document.getElementById(formId);
		if (!formElement) {
		    console.error('Form not found');
		    return;
		}
		var fileInput = formElement.querySelector('input[name="photoFileId"]');
		var hasFile = fileInput && fileInput.files && fileInput.files.length > 0;
		
		var pageParams =getEntities();
		
	    if (hasFile) {
	        // --- If photo file present, use FormData ---
	        dataToSend = new FormData(form[0]);
	        processData = false;
	        contentType = false;
			dataToSend.append('pageParams', pageParams);
	    } else {
	        // --- Otherwise, serialize normal form data ---
	        dataToSend = form.serialize();
	        processData = true;
	        contentType = 'application/x-www-form-urlencoded; charset=UTF-8';
			dataToSend += '&pageParams=' + encodeURIComponent(pageParams);
	    }
		
		
		

	    var actionUrl = form.attr('action');
		var refIdElement = formElement.querySelector('#ref_id');
		if (refIdElement && refIdElement.value) {
		    actionUrl += '/' + refIdElement.value;
		} else if (refIdElement && !refIdElement.value) {
		    console.error('Reference ID not found');
		    return;
		}

	    console.log('Submitting to:', actionUrl);

	    $.ajax({
	        type: 'POST',
	        url: actionUrl,
	        data: dataToSend,
	        processData: processData,
	        contentType: contentType,
	        dataType: 'json',
	        success: function(response) {
	            if (response.status === 'error') {
	                handleFormErrors(response);
	            } else if (response.status === 'success') {
	                $('#dynamicModal').modal('hide');
	                $('#nestedModal').modal('hide');
	                Swal.fire({
	                    title: 'Success',
	                    html: response.message,
	                    icon: 'success',
	                    confirmButtonText: 'Ok',
	                    allowOutsideClick: false
	                }).then((result) => {
	                    if (result.isConfirmed) {
	                        if (response.loadnext) {
	                            if (response.loadnext === 'reload') {
	                                window.location.reload();
	                                return;
	                            }
	                            if (response.target === 'modal' || response.target === 'modal2') {
	                                loadContent(response.loadnext, response.target);
	                            } else {
	                                loadContent(response.loadnext, '#' + (response.target || 'content'));
	                                form.parent().html('');
	                            }
	                        }
	                    }
	                });
	            }
	        },
	        error: function(xhr) {
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
	    $('input, select, textarea').removeClass('has-error'); // Remove error classes

	    // Display the general error message
	    if (response.message) {
	        $('#error-messages').append('<p class="error-message" style="color:red;">' + response.message + '</p>');
	    }

	    // Iterate through each error in the response
	    $.each(response.errors, function(field, error) {
	        // Use the field name directly (no snake_case conversion)
	        var fieldElement = $('[name="' + field + '"]');

	        if (fieldElement.length > 0) {
	            // Highlight the field
	            fieldElement.addClass('has-error');

	            // Special case: radio or checkbox → show message after the group
	            if (fieldElement.attr('type') === 'radio' || fieldElement.attr('type') === 'checkbox') {
	                fieldElement.last().closest('.form-check-inline, .form-check, div')
	                    .after('<span class="error-message" style="color:red;">' + error + '</span>');
	            } else {
	                // Regular input/select/textarea → show message right after field
	                fieldElement.after('<span class="error-message" style="color:red;">' + error + '</span>');
	            }
	        } else {
	            // If the field is not found, append the error message to the container
	            $('#error-messages').append('<p class="error-message" style="color:red;">' + error + '</p>');
	        }
	    });
	}









	document.addEventListener('focusin', function (e) {
	  if (e.target.tagName === 'INPUT') {
	    e.target.classList.remove('has-error');

	    const next = e.target.nextElementSibling;
	    if (next && next.classList.contains('error-message')) {
	      next.remove();
	    }
	  }
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
    // find the nearest form so the button can live anywhere inside it
    var $form     = $('button[onclick*="searchEntities"]').closest('form');
    var sortField = $('.sort.active').data('field') || defaultSortField;
    var sortDir   = $('.sort.active').data('dir')   || 'asc';
    var page      = $('.pagination .active').find('a').data('page') || 0;

    // build query from every form element that has a name
    const params = new URLSearchParams();
    params.append('page', page);
    params.append('sortField', sortField);
    params.append('sortDir', sortDir);

    $form.find(':input[name]').each(function () {
        var name  = this.name;
        let   value = $(this).val();
        if (value !== null && value !== '') {          // skip empty
            params.append(name, value);
        }
    });

    const url = listingUrl + '?' + params.toString();
    loadContent(url, targetContainer);
}


function getEntities() {
    // Get the form
    var $form = $('#search-form');

    // Determine sorting
    var $activeSort = $('.sort.active');
    var sortField   = $activeSort.data('field');
    var sortDir     = $activeSort.data('dir') || 'asc';

    // Determine current page
    var page = parseInt($('.pagination .active .page-link').text(), 10) || 0;

    // Build query string
    const params = new URLSearchParams();
    params.append('page', page-1);
    params.append('sortField', sortField);
    params.append('sortDir', sortDir);

    // Append form inputs
    $form.find(':input[name]').each(function () {
        var name  = this.name;
        var value = $(this).val();
        if (value !== null && value !== '') {
            params.append(name, value);
        }
    });

    return params.toString(); // returns like: "page=0&sortField=desigId&sortDir=asc&level=COUNTRY"
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
    const targetContainer = '#content';

    const $link = $('a.sort[data-field="' + sortField + '"]');

    // Toggle direction based on current attribute
    const sortDir = $link.attr('data-dir') === 'asc' ? 'desc' : 'asc';
    $link.attr('data-dir', sortDir);

    // Reset others
    $('a.sort').not($link).each(function () {
        $(this)
            .removeClass('active')
            .attr('data-dir', 'asc')
            .find('i')
            .removeClass('fa-sort-up fa-sort-down')
            .addClass('fa-sort');
    });

    // Mark this link active
    $link.addClass('active');

    // Update icon for this column
    const $icon = $link.find('i');
    $icon.removeClass('fa-sort fa-sort-up fa-sort-down');
    $icon.addClass(sortDir === 'asc' ? 'fa-sort-up' : 'fa-sort-down');

    // Trigger your search / reload
    searchEntities(listingUrl, targetContainer, sortField, sortDir);
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
				Swal.fire({
					title: 'Success',
					html: data.message,
					icon: 'success',
					confirmButtonText: 'Ok',
					allowOutsideClick: false
				}).then((result) => {
					if (result.isConfirmed) {
						if (data.loadnext) {

							if (data.loadnext === 'reload') {
								window.location.reload();
								return;
							}

							if (data.target === 'modal') {
								loadContent(data.loadnext, data.target);
							} else {
								loadContent(data.loadnext, '#' + (data.target || 'content'));
								form.parent().html('');
							}
						}
					}
				});

			}



			else if (isModal) {
				// modal path

				//const $modal = (targetSelector === 'modal') ? ($target.is('.modal') ? $target : $('#dynamicModal')) : $('#nestedModal')
				
				var targetEl = $target instanceof jQuery ? $target[0] : $target;

				var modal =
				  targetSelector === 'modal'
				    ? (targetEl && targetEl.classList.contains('modal') ? targetEl : document.getElementById('dynamicModal'))
				    : document.getElementById('nestedModal');
				$modal = $(modal);					

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
				/*setTimeout(() => {
					const $first = $modal.find('input,select,textarea').filter(':visible').first();
					if ($first.length) $first.focus();
				}, 300);*/
				
				var fields = modal.querySelectorAll('input, select, textarea');
				var firstVisible = Array.from(fields).find(el => el.offsetParent !== null);
				if (firstVisible) {
				  firstVisible.focus();
				  }
				
				
				
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



function showAll(listingUrl, targetContainer, defaultSortField) {
  // Clear all input fields
  document.querySelectorAll('input[type="text"], input[type="number"], input[type="email"], textarea').forEach(el => el.value = '');

  // Clear all checkboxes and radios
  document.querySelectorAll('input[type="checkbox"], input[type="radio"]').forEach(el => el.checked = false);

  // Reset all select elements
  document.querySelectorAll('select').forEach(el => el.selectedIndex = 0);

  // Call the searchEntities function
  searchEntities(listingUrl, targetContainer, defaultSortField);
}


function closeNestedModalOnly() {
	const nestedModal = bootstrap.Modal.getInstance('#nestedModal');
	$('#nestedModal').modal('hide');
}




// Overlay functions
function displayOverlay() {
    if (!document.getElementById('overlay')) {
        const overlay = document.createElement('div');
        overlay.id = 'overlay';
        overlay.textContent = 'Loading.....';
        Object.assign(overlay.style, {
            position: 'fixed',
            top: '0',
            left: '0',
            width: '100%',
            height: '100%',
            backgroundColor: 'rgba(0,0,0,0.5)',
            zIndex: '2500',
            color: '#fff',
            fontSize: '40px',
            fontWeight: 'bold',
            display: 'flex',
            justifyContent: 'center',
            alignItems: 'center',
            cursor: 'wait'
        });
        document.body.appendChild(overlay);
    }
}

function removeOverlay() {
    const overlay = document.getElementById('overlay');
    if (overlay) document.body.removeChild(overlay);
}


