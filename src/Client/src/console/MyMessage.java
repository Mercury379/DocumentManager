package console;

import java.io.Serializable;

public class MyMessage implements Serializable {
    private String operation;
    private String subOperation;
    private Object data;
    private String state;
    public String toString() {
        return "Mymessage{" +
                "operation='" + operation + '\'' +
                ", subOperation='" + subOperation + '\'' +
                ", data='" + data + '\'' +",state='"+state+'\''+
                '}';
    }
    public String getOperation(){
        return operation;
    }
    public void setOperation(String operation){
        this.operation=operation;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getSubOperation() {
        return subOperation;
    }

    public void setSubOperation(String subOperation) {
        this.subOperation = subOperation;
    }

}
