window.log=function(){log.history=log.history||[];log.history.push(arguments);if(this.console){arguments.callee=arguments.callee.caller;var a=[].slice.call(arguments);(typeof console.log==="object"?log.apply.call(console.log,console,a):console.log.apply(console,a))}};(function(e){function h(){}for(var g="assert,count,debug,dir,dirxml,error,exception,group,groupCollapsed,groupEnd,info,log,timeStamp,profile,profileEnd,time,timeEnd,trace,warn".split(","),f;f=g.pop();){e[f]=e[f]||h}})((function(){try{console.log();return window.console}catch(a){return window.console={}}})());var Survey=Backbone.Model.extend({initialize:function(){console.log("A single survey model was created")}});var SurveyList=Backbone.Collection.extend({initialize:function(){console.log("A survey collection was created")},url:function(){return"/application/listsurveys"},model:Survey});var App={init:function(){var a=new SurveyList({});a.fetch({success:loadApp})}};$(document).ready(function(){App.init()});