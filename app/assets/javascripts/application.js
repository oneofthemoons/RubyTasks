// This is a manifest file that'll be compiled into application.js, which will include all the files
// listed below.
//
// Any JavaScript/Coffee file within this directory, lib/assets/javascripts, or any plugin's
// vendor/assets/javascripts directory can be referenced here using a relative path.
//
// It's not advisable to add code directly here, but if you do, it'll appear at the bottom of the
// compiled file. JavaScript code in this file should be added after the last require_* statement.
//
// Read Sprockets README (https://github.com/rails/sprockets#sprockets-directives) for details
// about supported directives.
//
//= require jquery3
//= require jquery_ujs
//= require activestorage
//= require_tree
//= require 'icheck'
//= require select2

$(document).ready(function() {
	$('.cn').hide();
	$('#projectDiv').hide();
	$(".todo-btn").click(function(event) {
		event.preventDefault();
		$('.cn').show();
	});
	$(".project-btn").click(function(event) {
		event.preventDefault();
		$('#projectDiv').show();
		$('#projectBtnDiv').hide();
	});
	$(".cancel-project").click(function(event) {
		event.preventDefault();
		$('#projectDiv').hide();
		$('#projectBtnDiv').show();
	});
	$(".cancel-todo").click(function(event) {
		event.preventDefault();
		$('.cn').hide();
	});
	$(".create-proj").click(function(event) {
		event.preventDefault();
		$('#project-form').submit();
	});
	$('select').select2({
		minimumResultsForSearch: -1
	});
	
	$('input').on('ifChecked', function(event){
		$(this).parent().parent().children('#TodoTextDiv').addClass('strikeClass');
	});

	$('input').on('ifUnchecked', function(event){
		$(this).parent().parent().children('#TodoTextDiv').removeClass('strikeClass');
	});
});

function icheck(){
	if($(".icheck-me").length > 0){
	  $(".icheck-me").each(function(){
		var $el = $(this);
		var skin = ($el.attr('data-skin') !== undefined) ? "_" + $el.attr('data-skin') : "",
		color = ($el.attr('data-color') !== undefined) ? "-" + $el.attr('data-color') : "";
		var opt = {
		  checkboxClass: 'icheckbox' + skin + color,
		  radioClass: 'iradio' + skin + color,
		}
		$el.iCheck(opt);
	  });
	}
}
  
$(function(){
	icheck();
})
