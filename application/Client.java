import java.net.*; 
import java.io.*; 
import java.util.*; 
public class Client { 
    private static final String EXIT = "Exit"; 
    static String username; 
    static volatile boolean finished = false; 
    private static Scanner in = new Scanner(System.in); 
    
    public static void main(String[]args) 
    { 
      System.out.println();
      System.out.println("Você deseja acessar a sala de bate-papo?");
      System.out.println("1 - SIM | 2 - NÃO");
      int answer = in.nextInt();

      if (answer == 1){
      try { 
            InetAddress group = InetAddress.getByName("239.0.0.0");
            int port = Integer.parseInt("4200"); 

            System.out.println();
            System.out.println("Olá");
          
            System.out.println("Você está conectado no HOST: "+group.getHostAddress()+" - PORTA: "+port+ "\n");
            System.out.println("Insira seu nome: "); 
            username = in.next(); 
        
            MulticastSocket socket = new MulticastSocket(port); 
            socket.setTimeToLive(0);                   
            socket.joinGroup(group); 
            Thread thread = new Thread(new Server(socket,group,port)); 
            thread.start();    
            System.out.println(""+username+" conectado \n");                   

            while(true) { 
              String message; 
              message = in.nextLine(); 
              if(message.equalsIgnoreCase(Client.EXIT)) { 
                finished = true; 
                socket.leaveGroup(group); 
                socket.close(); 
                break; 
              } 
              if (!message.isEmpty()){
                message = username + ": " + message;
              }
              byte[] buffer = message.getBytes(); 
              DatagramPacket datagram = new
              DatagramPacket(buffer,buffer.length,group,port); 
              socket.send(datagram); 
            } 
          } catch(SocketException e) { 
            System.out.println("Não foi possivel conectar!"); 
          } catch(IOException e){ 
            System.out.println("Erro no sistema."); 
          } 
      } else {
        System.out.println();
        System.out.println("Você foi desconectado.");
      }
    }
} 
