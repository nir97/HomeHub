$(document).ready(function(){
  var date_input=$('input[name="user_birthdate"]'); //our date input has the name "date"
  var container=$('.bootstrap-iso form').length>0 ? $('.bootstrap-iso form').parent() : "body";
  date_input.datepicker({
    format: 'mm/dd/yyyy',
    container: container,
    todayHighlight: true,
    autoclose: true,
  })
})



$(".js-select1").each(function(){
			$(this).select2({
				minimumResultsForSearch: 20,
				dropdownParent: $(this).next('.dropDownSelect1')
			});


			$(".js-select1").each(function(){
				$(this).on('select1:close', function (e){
					if($(this).val() == "Please chooses") {
						$('.js-show-service').slideUp();
					}
					else {
						$('.js-show-service').slideUp();
						$('.js-show-service').slideDown();
					}
				});
			});
		})

    $(".js-select2").each(function(){
    			$(this).select2({
    				minimumResultsForSearch: 20,
    				dropdownParent: $(this).next('.dropDownSelect2')
    			});


    			$(".js-select2").each(function(){
    				$(this).on('select2:close', function (e){
    					if($(this).val() == "Please chooses") {
    						$('.js-show-service').slideUp();
    					}
    					else {
    						$('.js-show-service').slideUp();
    						$('.js-show-service').slideDown();
    					}
    				});
    			});
    		})

        function w3_open() {
          var x = document.getElementById("mySidebar");
          x.style.width = "300px";
          x.style.paddingTop = "10%";
          x.style.display = "block";
        }

        // Close side navigation
        function w3_close() {
          document.getElementById("mySidebar").style.display = "none";
        }

        // Used to toggle the menu on smaller screens when clicking on the menu button
        function openNav() {
          var x = document.getElementById("navDemo");
          if (x.className.indexOf("w3-show") == -1) {
            x.className += " w3-show";
          } else {
            x.className = x.className.replace(" w3-show", "");
          }
        }

        function myAccFunc() {
          var x = document.getElementById("demoAcc");
          if (x.className.indexOf("w3-show") == -1) {
            x.className += " w3-show";
          } else {
            x.className = x.className.replace(" w3-show", "");
          }
        }


        var req;
        var is_IE;

        var search_id;
        var complete_table;
        var auto_row;

        function init() {
            search_id = document.getElementById("searchBar");
            complete_table = document.getElementById("complete-table");
            auto_row = document.getElementById("auto-row");
        }

        function doCompletion() {
            var url = "autocomplete?action=complete&search_id=" + escape(search_id.value);
            req = initRequest();
            req.open("GET", url, true);
            req.onreadystatechange = callback;
            req.send(null);
        }

        function initRequest() {
            if (window.XMLHttpRequest) {
                if (navigator.userAgent.indexOf('MSIE') != -1) {
                    is_IE = true;
                }
                return new XMLHttpRequest();
            } else if (window.ActiveXObject) {
                is_IE = true;
                return new ActiveXObject("Microsoft.XMLHTTP");
            }
        }

        function callback() {
            clearTable();
            if (req.readyState == 4) {
                if (req.status == 200) {
                    parseMessages(req.responseXML);
                }
            }
        }

        function clearTable() {
            if (complete_table.getElementsByTagName("tr").length > 0) {
                complete_table.style.display = 'none';
                for (loop = complete_table.childNodes.length -1; loop >= 0 ; loop--) {
                    complete_table.removeChild(complete_table.childNodes[loop]);
                }
            }
        }

        function parseMessages(responseXML) {

            if (responseXML == null) {
                return false;
            } else {

                var services = responseXML.getElementsByTagName("services")[0];

                if (services.childNodes.length > 0) {
                    complete_table.setAttribute("bordercolor", "white");
                    complete_table.setAttribute("border", "1");

                    for (loop = 0; loop < services.childNodes.length; loop++) {
                        var service = services.childNodes[loop];
                        var service_id = service.getElementsByTagName("service_id")[0];
                        var service_provider_name = service.getElementsByTagName("service_provider_name")[0];
                        var service_name = service.getElementsByTagName("service_name")[0];
                        var service_category_super = service.getElementsByTagName("service_category_super")[0];
                        var service_category_sub = service.getElementsByTagName("service_category_sub")[0];
                        appendProduct(service_id.childNodes[0].nodeValue, service_provider_name.childNodes[0].nodeValue, service_name.childNodes[0].nodeValue, service_category_super.childNodes[0].nodeValue, service_category_sub.childNodes[0].nodeValue);
                    }
                }
            }
        }

        function appendProduct(service_id, service_provider_name, service_name, service_category_super, service_category_sub) {

            var row;
            var cell;
            var link_element;

            if (is_IE) {
                complete_table.style.display = 'block';
                row = complete_table.insertRow(complete_table.rows.length);
                cell = row.insertCell(0);
            } else {
                complete_table.style.display = 'table';
                row = document.createElement("tr");
                cell = document.createElement("td");
                row.appendChild(cell);
                complete_table.appendChild(row);
            }

            cell.className = "popupCell";

            link_element = document.createElement("a");
            link_element.className = "popupItem";
            link_element.setAttribute("href", "autocomplete?action=lookup&search_id=" + service_id);
            var text = service_provider_name + " - " + service_name + " - " + service_category_super + " - " + service_category_sub;
            link_element.appendChild(document.createTextNode(text));
            cell.appendChild(link_element);
        }
