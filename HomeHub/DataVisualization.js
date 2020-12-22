google.charts.load('current', {packages: ['corechart', 'bar', 'table']});

$("#btnTableServices").click(function () {
	var x = document.getElementById("table_services");
	if(x.style.display == "none") {
		x.style.display = "block";
		document.getElementById("btnTableServices").innerHTML = "Close Table of Services";
		$.ajax({
			url: "DataVisualization",
			type: "POST",
			data: "action=table_services",
			success: function (msg) {
				createData("table_services", msg)
			},
			error: function(){
				console.log("error occurred while making ajax call")
			}
		});
	}else if(x.style.display == "block"){
		x.style.display = "none";
		document.getElementById("btnTableServices").innerHTML = "View Table of Services";
	}
});

$("#btnChartServices").click(function () {
	var x = document.getElementById("chart_services");
	if(x.style.display == "none") {
		x.style.display = "block";
		document.getElementById("btnChartServices").innerHTML = "Close Chart of Services";
		$.ajax({
			url: "DataVisualization",
			type: "POST",
			data: "action=chart_services",
			success: function (msg) {
				createData("chart_services", msg)
			},
			error: function(){
				console.log("error occurred while making ajax call")
			}
		});
	}else if(x.style.display == "block"){
		x.style.display = "none";
		document.getElementById("btnChartServices").innerHTML = "View Chart of Services";
	}
});

$("#btnTableServiceSold").click(function () {
	var x = document.getElementById("table_service_sold");
	if(x.style.display == "none") {
		x.style.display = "block";
		document.getElementById("btnTableServiceSold").innerHTML = "Close Table of Service Sold";
		$.ajax({
			url: "DataVisualization",
			type: "POST",
			data: "action=table_service_sold",
			success: function (msg) {
				createData("table_service_sold", msg)
			},
			error: function(){
				console.log("error occurred while making ajax call")
			}
		});
	}else if(x.style.display == "block"){
		x.style.display = "none";
		document.getElementById("btnTableServiceSold").innerHTML = "View Table of Service Sold";
	}
});

$("#btnChartServiceSold").click(function () {
	var x = document.getElementById("chart_service_Sold");
	if(x.style.display == "none") {
		x.style.display = "block";
		document.getElementById("btnChartServiceSold").innerHTML = "Close Chart of Service Sold";
		$.ajax({
			url: "DataVisualization",
			type: "POST",
			data: "action=chart_service_Sold",
			success: function (msg) {
				createData("chart_service_Sold", msg)
			},
			error: function(){
				console.log("error occurred while making ajax call")
			}
		});
	}else if(x.style.display == "block"){
		x.style.display = "none";
		document.getElementById("btnChartServiceSold").innerHTML = "View Chart of Service Sold";
	}
});

$("#btnTableDailySalesTransaction").click(function () {
	var x = document.getElementById("table_daily_sales_transaction");
	if(x.style.display == "none") {
		x.style.display = "block";
		document.getElementById("btnTableDailySalesTransaction").innerHTML = "Close Table of Daily Dales Transaction";
		$.ajax({
			url: "DataVisualization",
			type: "POST",
			data: "action=table_daily_sales_transaction",
			success: function (msg) {
				createData("table_daily_sales_transaction", msg)
			},
			error: function(){
				console.log("error occurred while making ajax call")
			}
		});
	}else if(x.style.display == "block"){
		x.style.display = "none";
		document.getElementById("btnTableDailySalesTransaction").innerHTML = "View Table of Daily Dales Transaction";
	}
});



function createData(id, jsonData) {

	if(id == "table_services"){
		var parsed_data = $.parseJSON(jsonData);
		var data_arr = new Array();
		for(var i=0; i<parsed_data.length; i++) {
			var cat_super = parsed_data[i]["category super"];
			var cat_sub = parsed_data[i]["category sub"];
			var service_count = parsed_data[i]["service count"];
			data_arr.push( [ cat_super , cat_sub , service_count ] );
		}
		drawTable(id, data_arr);
	}

	if(id == "chart_services"){
		var parsed_data = $.parseJSON(jsonData);
		var data_arr = new Array();
		for(var i=0; i<parsed_data.length; i++) {
			var cat_super = parsed_data[i]["category super"];
			var cat_sub = parsed_data[i]["category sub"];
			var service_count = parsed_data[i]["service count"];
			data_arr.push( [ "("+cat_super+","+cat_sub+")" , service_count ] );
		}
		drawChart(id, data_arr);
	}

	if(id == "table_service_sold"){
		var parsed_data = $.parseJSON(jsonData);
		var data_arr = new Array();
		for(var i=0; i<parsed_data.length; i++) {
			var service_name = parsed_data[i]["name"];
			var service_rate = parsed_data[i]["rate"];
			var service_hours = parsed_data[i]["hours"];
			var service_sales = parsed_data[i]["sales"];
			data_arr.push( [ service_name , service_rate , service_hours, service_sales ] );
		}
		drawTable(id, data_arr);
	}

	if(id == "chart_service_Sold"){
		var parsed_data = $.parseJSON(jsonData);
		var data_arr = new Array();
		for(var i=0; i<parsed_data.length; i++) {
			var service_name = parsed_data[i]["name"];
			var service_sales = parsed_data[i]["sales"];
			data_arr.push( [ service_name , service_sales ] );
		}
		drawChart(id, data_arr);
	}

	if(id == "table_daily_sales_transaction"){
		var parsed_data = $.parseJSON(jsonData);
		var data_arr = new Array();
		for(var i=0; i<parsed_data.length; i++) {
			var date = parsed_data[i]["date"];
			var sales = parsed_data[i]["sales"];
			data_arr.push( [ date , sales ] );
		}
		drawTable(id, data_arr);
	}

}

function drawTable(id, data){

	if(id == "table_services"){
		var table_data = new google.visualization.DataTable();
		table_data.addColumn('string', 'category super');
		table_data.addColumn('string', 'category sub');
		table_data.addColumn('number', 'service count');
		table_data.addRows(data);
		var options = {
			'width': 600,
			'height': 650,
			'showRowNumber': true,
		};
		var table = new google.visualization.Table(document.getElementById(id));
		table.draw(table_data, options);
	}

	if(id == "table_service_sold"){
		var table_data = new google.visualization.DataTable();
		table_data.addColumn('string', 'Service Name');
    table_data.addColumn('number', 'Service Rate');
		table_data.addColumn('number', 'Service Hours');
		table_data.addColumn('number', 'Service Sales');
		table_data.addRows(data);
		var options = {
			'width': 600,
			'height': 650,
			'showRowNumber': true,
		};
		var table = new google.visualization.Table(document.getElementById(id));
		table.draw(table_data, options);
	}

	if(id == "table_daily_sales_transaction"){
		var table_data = new google.visualization.DataTable();
		table_data.addColumn('string', 'Date');
    table_data.addColumn('number', 'Sales');
		table_data.addRows(data);
		var options = {
			'width': 600,
			'height': 650,
			'showRowNumber': true,
		};
		var table = new google.visualization.Table(document.getElementById(id));
		table.draw(table_data, options);
	}

}

function drawChart(id , data) {

	if(id == "chart_services"){
		data.unshift(["category-(super,sub)", "service count"]);
		var chart_data = google.visualization.arrayToDataTable(data);
		var options = {
			'width':800,
			'height':800,
			title: "Service Count Chart",
			bars: 'horizontal'
		};
		var chart = new google.visualization.BarChart(document.getElementById(id));
		chart.draw(chart_data, options);
	}

	if(id == "chart_service_Sold"){
		data.unshift(["Service Name", "Service Sales"]);
		var chart_data = google.visualization.arrayToDataTable(data);
		var options = {
			'width':800,
			'height':800,
			title: "Service Sales Chart",
			bars: 'horizontal'
		};
		var chart = new google.visualization.BarChart(document.getElementById(id));
		chart.draw(chart_data, options);
	}

}
