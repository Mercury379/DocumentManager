package service;

import console.DataProcessing;
import console.Doc;
import console.MyMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Enumeration;

public class SocketService {
    private Socket client;
    private int connectID;
    private ObjectOutputStream output;
    private ObjectInputStream input;

    public Socket getClient(){
        return client;
    }
    public SocketService(Socket socket,int id) throws IOException {
        try {
            client=socket;
            connectID=id;
            getStreams();
            processConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private void getStreams() throws IOException {
        output=new ObjectOutputStream(client.getOutputStream());
        output.flush();
        input=new ObjectInputStream(client.getInputStream());
        System.out.println("获得输入/输出流对象！");
    }
    public void processConnection() throws SQLException {
        while(true) {
            try {
                MyMessage message = (MyMessage) input.readObject();
                if (message.getOperation().equals("fileOperation")&&message.getState().equals("true")) {
                    DataProcessing.fileOperation((String) message.getData(), message.getSubOperation(), true);
                } else if (message.getOperation().equals("fileOperation")&&message.getState().equals("false")){
                    DataProcessing.fileOperation((String) message.getData(), "", false);
                }else if(message.getOperation().equals("searchDoc")){
                    message.setData((Object) DataProcessing.searchDoc((String)message.getData()));
                }else if(message.getOperation().equals("listDoc")){
                    message.setData((Object)DataProcessing.listDoc());
                }
                output.writeObject((Object) message);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

    }

}
