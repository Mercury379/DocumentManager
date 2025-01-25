# DocumentManager
WHUT面向对象与多线程实验，GUI编程涉及到了一些客户端服务器的网络编程知识，当时真的学到了很多。


### 简介
在Server包中，我设计了MyMessage类用于规范客户机服务器传递消息的规范模式，在MyMessage类中定义了私有成员operation、subOperation、data和state，以及相应的get和set方法并且重写了toString方法。   

     
在Server包中创建了SocketService类用于接收并处理客户机收到的消息，在SocketService中定义了getStreams()方法，用于建立客户机服务器之间的输入输出流并赋值于成员变量output和input。同时定义了processConnection()方法用于处理客户机传递过来的信息，通过input.readObject()读出客户机的消息，由于读出的为对象流，所以要将其强制转换为MyMessage并赋值给临时MyMessage变量message，通过判断message的operation是updateFile还是downloadFile接收相应的参数传递调用DataProcessing中的fileOperation执行相应的文件上传下载。   


为了实现一个服务器的多线程服务，在Server包中的ServerMain.java中创建了Threads类继承于Thread类，在Thread类设定私有静态整数变量countConnect并初始化为0，覆盖实现了run()方法，在run方法中每被调用一次，即与一个客户机相连接，打印出相关信息并创建一个SocketService对象用于与客户机传递消息（在SocketService构造方法中调用getStream()和processConnection()）。  


为了开启服务器，我设计了ServiceMain类，在其中的main函数中，首先需要调用DataProcessing.connectToDatabase()连接数据库，在connectToDatabase()中，需要加载加载数据库驱动类、声明数据库的URL并将设定自己的用户与密码分别赋值为driverName、url、user和password变量。再利用Class.forName(driverName)和DriverManager.getConnection(url,user,password)连接数据库并设定布尔值connectToDB为true表示已成功连接数据库。      


在连接完数据库后，利用ServerSocket(12345,100)创建ServerSocket变量server，由于可能有多个客户机连接服务器，所以需要一直监听是否有客户机连接，因此设置一个永真循环，并在永真循环中用server.accept()不断监听，一旦监听到客户机连接，便创建一个新Threads对象，用start()方法开启一个新线程。


然后运行Client客户端即可。

