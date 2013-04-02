import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Carrera 
{
	private ArrayList <Jugador> jugadores;
	private ArrayList <Caballo> caballos;
	//private Caballo c1;
	//private Caballo c2;
	private Cronometro cron;
	
	public Carrera()
	{
		jugadores = new ArrayList();
		caballos = new ArrayList();
        cron = new Cronometro();
	}
	
	public static void main(String[] args) 
	{
		BufferedReader teclado = new BufferedReader (new InputStreamReader(System.in));
		String respuesta = null;
		Carrera carrera= new Carrera ();
		
		carrera.crearJugadores();
		do{
			carrera.apostar();
			
			carrera.empezar();
			
			carrera.repartirPremios();
			
			

			try {
				 	System.out.print("Otra carrera? (S/N)");
				 	respuesta = teclado.readLine();
			} 
			catch (IOException e) {
				e.printStackTrace();
			}
			
		}while(respuesta.equalsIgnoreCase("s"));
		
		System.out.print("Gracias por usar las apuestas 'BetDaniel'");
		
		
	}
		
    public void empezar () 
    {
    	cron = new Cronometro();
    	int contador = 0;
    	int lastpos = 0;
    	
        // Arrancamos los caballos
        cron.start();
      
        for(int i=0; i<caballos.size(); i++){
			caballos.get(i).start();
		}
    
       contador = caballosVivos();
        
        // Esperamos a que el primero acabe
    	while (contador == caballos.size()){

    		int totPos;

    			totPos = totalPosiciones();
        		
    			if(lastpos != totPos){
    				for (int i=0; i<30; i++){
    					System.out.println();
    				}
    				
    				for(int i=0; i<caballos.size(); i++){
    					System.out.print("\n"+caballos.get(i).getNombre()+": ");
    					for(int j=0; j<caballos.get(i).getPosicion(); j++)
    		        	{
    		        		System.out.print("*");
    		        	}
            		}	
    			}
    			
    		lastpos = totalPosiciones();
    		
    		contador = caballosVivos();
    	}
    	
    	for(int i=0; i<caballos.size(); i++){
			if(caballos.get(i).isAlive() == true){
				caballos.get(i).stop();
			}
			else{
				System.out.println("\nEl caballo "+(i+1)+" es el ganador");
				caballos.get(i).setGanador(true);
			}
		}	
    	cron.stop();
        System.out.println("\nLa carrera ha durado: "+cron.getTime() + "\n");
        
    } 
    
    public void crearJugadores()
    {
    	Jugador juga;
    	
    	String linea = null;
    	int nJugadores = leerInt("Introducir numero de jugadores: ");

		for(int i=0; i<nJugadores; i++)
		{
			juga = new Jugador();
			juga.leerDatos();
			jugadores.add(juga);
			System.out.println("Jugador "+(i+1)+ " creado\n");
		}
    }
 
    public void apostar()
    {
    	caballos.clear();
    	Caballo caba;
    	boolean wrong;
    	float apuesta;
    	
    	int nCaballos = (int)(Math.random()*5 + 2);
    	System.out.println("En esta carrera hay "+nCaballos+" caballos");
    	
    	for(int i=0; i<nCaballos; i++){
			caba = new Caballo("Caballo "+(i+1));
			caba.setRatio((float)(Math.random()*3+1));
			caballos.add(caba);
			System.out.printf("El " + caba.getNombre() + " se paga a " + "%.2f\n",caba.getRatio());
		}
    	

    	for(int i=0; i<jugadores.size(); i++){
    		int c = 0;
    		do{
    			wrong = false;
    			c = leerInt("\n" + jugadores.get(i).getNombre()+ " introduce el numero del caballo ganador: ");
    			
    			if(c > nCaballos || c == 0){
    				System.out.println("Has introducido un caballo erroneo\n");
    				wrong = true;	
    			}
    		}while(wrong);
    		
    		jugadores.get(i).setCaballo(c-1);
    		
    		
    		if(jugadores.get(i).getSaldo() <= 0){
    			System.out.println("Tu saldo es de 0 o menos Euros y no podras participar.");
    		}
    		else{
    			System.out.println("Tu saldo es de " + jugadores.get(i).getSaldo()+ " euros.");
        		
        		do{
        			wrong = false;
        			apuesta = leerFloat("Introduce dinero a apostar al caballo "+(c)+": ");
        			
        			if(apuesta > jugadores.get(i).getSaldo() || apuesta <= 0){
        				System.out.println("Has apostado mas de lo que tienes o has puesto 0 o menos.\n");
        				wrong = true;	
        			}
    	
        		}while(wrong);
        		jugadores.get(i).setApuesta(apuesta);
    			
    		}
    	}
    } 
    
    public void repartirPremios()
    {
    	int ganador = 0;
    	
    	for(int i=0; i<caballos.size(); i++){
    		if(caballos.get(i).isGanador() == true){
    			ganador = i;
    		}
		}	
    	
    	for(int i=0; i<caballos.size(); i++){
			System.out.printf("El " + caballos.get(i).getNombre() + " se pagaba a " + "%.2f\n",caballos.get(i).getRatio());
		}
    	
    	for(int i=0; i<jugadores.size(); i++)
    	{
    		float ganancias = 0;
    		
	    	if(jugadores.get(i).getCaballo() == ganador)
	    	{
	    			ganancias = jugadores.get(i).getApuesta() + jugadores.get(i).getApuesta() * caballos.get(ganador).getRatio();
                    ganancias = (float)(Math.round(ganancias*100.0)/100.0);
	    			
	    			System.out.println(jugadores.get(i).getNombre() + " aposto por el caballo " + (jugadores.get(i).getCaballo()+1) + " y ha ganado");
	    			System.out.println(jugadores.get(i).getNombre() + " aposto " + jugadores.get(i).getApuesta() + " y ha ganado " + ganancias);
	    			
	    			jugadores.get(i).setSaldo(jugadores.get(i).getSaldo() + ganancias);
	    			
	    			System.out.println("Saldo total de " + jugadores.get(i).getNombre() + " " + jugadores.get(i).getSaldo() + " euros.\n");
	    	}
	    	else{
	    		System.out.println(jugadores.get(i).getNombre() + " aposto por el caballo " + (jugadores.get(i).getCaballo()+1) + " y no ha ganado.");
	    	}

    	}
    }
   
    public int totalPosiciones(){
    	
    	int totPos = 0;
		
		for(int i=0; i<caballos.size(); i++){
			
			totPos += caballos.get(i).getPosicion();
		}	
		
		return totPos;
    }
    
    public int caballosVivos(){
    	int contador = 0;
    	
    	for(int i=0; i<caballos.size(); i++){
			if(caballos.get(i).isAlive() == true){
				contador++;
			}	
		}
    	
    	return contador;
    }
    
    
    public int leerInt(String texto)
    {
    	String linea = null;
    	int numero = 0;

    	DataInputStream in = new DataInputStream(System.in);
    	
		try
		{
			System.out.print(texto);
			linea = in.readLine();
		}
		
		catch (IOException e)
		{
			System.out.println (e.getMessage());
		}
			
		try
		{
			numero = Integer.parseInt(linea);
		}
		catch (NumberFormatException e)
		{
			System.out.println (e.getMessage());
		}
    	
		return numero;
    }
    
    public float leerFloat(String texto)
    {
    	String linea = null;
    	float numero = 0;

    	DataInputStream in = new DataInputStream(System.in);
    	
		try
		{
			System.out.print(texto);
			linea = in.readLine();
		}
		
		catch (IOException e)
		{
			System.out.println (e.getMessage());
		}
			
		try
		{
			numero = Float.parseFloat(linea);
		}
		catch (NumberFormatException e)
		{
			System.out.println (e.getMessage());
		}
    	
		return numero;
    }
    
}