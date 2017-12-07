var MailUtil = Java.type("com.itshidu.jeelite.common.util.MailUtil");
var ScriptEngineUtil = Java.type("com.itshidu.jeelite.common.util.ScriptEngineUtil");
var Thread = Java.type("java.lang.Thread");
var Executors = Java.type("java.util.concurrent.Executors");
var TimeUnit = Java.type("java.util.concurrent.TimeUnit");

function Person(name,age){
	this.name = name;
	this.age = age;
	this.say = function(msg){
		print(msg);
	}
	return 'hhaa';
}
var a = new Person("悟空",18);
//ScriptEngineUtil.fun4(a);
print("HelloWorld");
new Thread(function(){
	
	while(true){
		Thread.sleep(1000);
		print("----------");
	}
}).start();

var service = Executors.newSingleThreadScheduledExecutor();
service.scheduleWithFixedDelay(function(){
	print("GoodBoy");
	
}, 1,1,TimeUnit.SECONDS);