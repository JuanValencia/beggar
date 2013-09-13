/*
 * MessageHolder.java
 *
 * Created on September 30, 2007, 4:45 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package beggar;

import java.awt.Color;

/**
 *
 * @author Juan
 */
public class MessageHolder {
    
    private final static int MAX_MESSAGES = 100;
    private final static Color DEFAULTCOLOR = Color.WHITE;
    
    public class Message {
        public Color color;
        public String message;
        public Message(String m, Color c) {
            color = c;
            message = m;
        }
    }
    
    /** Creates a new instance of MessageHolder */
    public MessageHolder() {
        messages = new Message[MAX_MESSAGES];
        messageIter = 0;
        messages[0] = null;
        totalMessages = 0;
    }    
    
    public void message(String s, Color c) {
        Message m = new Message(s, c);
        messages[messageIter] = m;
        messageIter++;
        totalMessages++;
        if (messageIter == MAX_MESSAGES) {
            messageIter = 0;
        }
    }
    
    public void message(String s) {
        message(s, DEFAULTCOLOR);
    }
    
    public void message(String s, double t, Color c) {
        String time = parseTime(t);
        Message m = new Message(time + s, c);
        messages[messageIter] = m;
        messageIter++;
        totalMessages++;
        if (messageIter == MAX_MESSAGES) {
            messageIter = 0;
        }
    }
    
    public void message(String s, double t) {
        message(s, t, DEFAULTCOLOR);
    }
    
    public Message getMessage(double t) {
        String time = parseTime(t);
        Message ans;
        try {
            if (messageIter > 0) {
                ans = new Message(time + messages[messageIter-1].message, messages[messageIter-1].color);
                return ans;
            }  else if ((messageIter == 0) && (messages[0] != null)) {
                ans = new Message(time + messages[MAX_MESSAGES-1].message, messages[MAX_MESSAGES-1].color);
                return ans;
            }
        } catch (Exception e) {
            System.err.println(e.toString());
        }
        return new Message("", DEFAULTCOLOR);
    }
    
    public Message[] getMessages(int num) {
        int total;
        int transIter = messageIter;
        total = num;
        Message[] answer;
        if (total>totalMessages) {
            total = totalMessages;
        }
        answer = new Message[total];
        
        for(int m = 0;m<total;m++) {
            if(transIter-m-1<0) {
                transIter = transIter + MAX_MESSAGES;
            }
            answer[m] = messages[transIter - m - 1];
        }   
        return answer;
    }
    
    private String parseTime(double t) {
        String ans = "(";
        int hours = (int)Math.floor(t/3600);
        ans += hours;
        ans += ":";
        int minutes = (int)Math.floor((t - (hours * 3600))/60);
        ans += minutes<10 ? "0" + minutes : minutes;
        ans += ":";
        int seconds = (int)(t - (hours * 3600) - (minutes * 60));
        ans += seconds<10 ? "0" + seconds : seconds;
        ans+= ") ";
        return ans;
    }
    
    private Message messages[];
    private int messageIter;
    private int totalMessages;
    
}
