package ap;
import java.util.*;

public class AP_assign3 {
	
	public static void main(String[] args) throws Exception{
		Game first=new Game();//new game is initiated
		first.gameplay();//game play starts by just this command
	}
	
}
abstract class Player implements Comparable<Player>{
	private boolean alive=true;//by default when Player is instantiated player is alive
	private int hp;
	private final int playerno;
	
	@Override
	public int compareTo(Player a) {//comparator has been modified as per our need for sorting
		return (this.gethealth()-a.gethealth());
	}
	
	@Override
	public boolean equals(Object a) {//equals method has been overridden to give results for equal class
		return this.getClass()==a.getClass();
	}
	
	Player(int n){
		this.playerno=n;
	}
	public int getplayerno() {
		return playerno;
	}
	public boolean getstatus() {
		return this.alive;
	}
	public int gethealth() {
		return hp;
	}
	public void sethealth(int hp) {
		this.hp=hp;
	}
	public void die() {
		this.alive=false;
	}
	public int uservote() {//this function is not really used
		Scanner in=new Scanner(System.in);
		System.out.println("Select a person to vote out:");
		int voteout=in.nextInt();
		return voteout;
	}
	public int botvote(ArrayList<Player> curplayers) {
		final ArrayList<Integer> healerscope=new ArrayList<Integer>();
		for(int i=0;i<curplayers.size();i++) {
			if(curplayers.get(i).getstatus()) {
				healerscope.add(curplayers.get(i).getplayerno());
			}
		}
		Random rand=new Random();
		if(healerscope.size()!=0) {
			return healerscope.get(rand.nextInt(healerscope.size()));
		}//returns the player number to target
		else return -1;
	}
	
	abstract public int botplay(ArrayList<Player> curplayers);
	abstract public int userplay();
}

class Mafia extends Player{
	
	Mafia(int n){
		super(n);
		super.sethealth(2500);
	}
	
	@Override
	public int botplay(ArrayList<Player> curplayers) {
		final ArrayList<Integer> mafiascope=new ArrayList<Integer>();
			for(int i=0;i<curplayers.size();i++) {
				if(curplayers.get(i).getstatus()&&(!curplayers.get(i).equals(this))) {
					mafiascope.add(curplayers.get(i).getplayerno());
				}
			}
		Random rand=new Random();
		if(mafiascope.size()!=0) {
		return mafiascope.get(rand.nextInt(mafiascope.size()));}//returns the player number to target
		else return -1;
	}
	@Override
	public int userplay() {
		Scanner in=new Scanner(System.in);
		System.out.println("Choose a target:");
		int target=in.nextInt();
		return target;
	}
}

class Healer extends Player{
	
	Healer(int n){
		super(n);
		super.sethealth(800);
	}
	@Override
	public int botplay(ArrayList<Player> curplayers) {
		final ArrayList<Integer> healerscope=new ArrayList<Integer>();
		for(int i=0;i<curplayers.size();i++) {
			if(curplayers.get(i).getstatus()) {
				healerscope.add(curplayers.get(i).getplayerno());
			}
		}
		Random rand=new Random();
		if(healerscope.size()!=0) {
			return healerscope.get(rand.nextInt(healerscope.size()));
		}//returns the player number to target
		else return -1;
	}
	@Override
	public int userplay() {
		Scanner in=new Scanner(System.in);
		System.out.println("Choose a player to heal:");
		int toheal=in.nextInt();
		return toheal;
	}
}
class Detective extends Player{
	
	Detective(int n){
		super(n);
		super.sethealth(800);
	}
	@Override
	public int botplay(ArrayList<Player> curplayers) {
		final ArrayList<Integer> detectivescope=new ArrayList<Integer>();
		for(int i=0;i<curplayers.size();i++) {
			if(curplayers.get(i).getstatus()&&(!curplayers.get(i).equals(this))) {
				detectivescope.add(curplayers.get(i).getplayerno());
			}
		}
		Random rand=new Random();
		if(detectivescope.size()!=0) {
			return detectivescope.get(rand.nextInt(detectivescope.size()));
		}//returns the player number to target
		else return -1;
	}
	@Override
	public int userplay() {
		Scanner in=new Scanner(System.in);
		System.out.println("Choose a player to test:");
		int totest=in.nextInt();
		return totest;
	}	
}
class Common extends Player{
	Common(int n){
		super(n);
		super.sethealth(1000);
	}
	@Override
	public int botplay(ArrayList<Player> curplayers) {//this function is not really used
		return -1;
	}
	@Override
	public int userplay() {//this function is not really used
		return -1;		
	}
	
}
class Game{
	private int round;
	private int lastcaughtmafiaindex;
	private int totalalive;
	private ArrayList<Player> curplayers=new ArrayList<Player>();
	private ArrayList<Player> allplayers=new ArrayList<Player>();
	private int number;
	private int mafias;
	private int detectives;
	private int commoners;
	private int healers;
	private final Player user;
	private ArrayList<Integer> positions=new ArrayList<Integer>();
	
	Game(){
		user=shuffler();
		round=1;
		totalalive=number;
	}
	
	public void gameplay() {
		while(mafiasalive()>0&&mafiasalive()<gettotalalive()-mafiasalive()) {
			play();
			nextround();
			System.out.println();
		}
		
		if(mafiasalive()==0) {
			System.out.println("The Mafias have lost.");
		}
		else {
			System.out.println("The Mafias have won.");
		}
//		printinfo();
		printinfo(allplayers);
	}
	
	private void printinfo(ArrayList<? extends Player> hello) {//GENERIC
		for(int i=0;i<number;i++) {
			if(hello.get(i).equals(new Mafia(0))) {
			System.out.print(" Player"+hello.get(i).getplayerno()+",");}
		}System.out.println(" were mafias");
		for(int i=0;i<number;i++) {
			if(hello.get(i).equals(new Detective(0))) {
			System.out.print(" Player"+hello.get(i).getplayerno()+",");}
		}System.out.println(" were Detectives");
		for(int i=0;i<number;i++) {
			if(hello.get(i).equals(new Healer(0))) {
			System.out.print(" Player"+hello.get(i).getplayerno()+",");}
		}System.out.println(" were Healers");
		for(int i=0;i<number;i++) {
			if(hello.get(i).equals(new Common(0))) {
			System.out.print(" Player"+hello.get(i).getplayerno()+",");}
		}System.out.println(" were Commoners");
//		for(int i=0;i<number;i++) {
//			if(curplayers.get(i).equals(new Mafia(0))) {
//			System.out.print(" Player"+curplayers.get(i).getplayerno()+",");}
//		}System.out.println(" were mafias");
//		for(int i=0;i<number;i++) {
//			if(curplayers.get(i).equals(new Detective(0))) {
//			System.out.print(" Player"+curplayers.get(i).getplayerno()+",");}
//		}System.out.println(" were Detectives");
//		for(int i=0;i<number;i++) {
//			if(curplayers.get(i).equals(new Healer(0))) {
//			System.out.print(" Player"+curplayers.get(i).getplayerno()+",");}
//		}System.out.println(" were Healers");
//		for(int i=0;i<number;i++) {
//			if(curplayers.get(i).equals(new Common(0))) {
//			System.out.print(" Player"+curplayers.get(i).getplayerno()+",");}
//		}System.out.println(" were Commoners");
	}
	
	private int gettotalalive() {
		return totalalive;
	}
	
	private void nextround() {
		round++;
	}
	
	private int mafiasalive() {
		int mafalive=0;
		for(int i=0;i<number;i++) {
			if(curplayers.get(i).equals(new Mafia(0))&&curplayers.get(i).getstatus()) {
			mafalive++;	
			}
		}
		return mafalive;
	}
	
	private void play() {//the function managing the chances of all players
		
		System.out.print("Round" +round+":\n"+ totalalive +"players are remaining:");
		for(int i=0;i<curplayers.size();i++) {
			if(curplayers.get(i).getstatus()) {
				System.out.print("Player"+curplayers.get(i).getplayerno()+" ,");
			}
		}System.out.println("are alive.");
		
		mafiaschance();
		boolean mafiacaught=detectiveschance();
		healerchance();
		System.out.println("--End of actions--");
		deathchecker();
		if(mafiacaught) {
			System.out.println("Player"+curplayers.get(lastcaughtmafiaindex).getplayerno()+" has been voted out.");
//			curplayers.get(lastcaughtmafiaindex).die();//already subtracted from totalalive in the detectivechance function
		}
		else {//if mafia was not caught by the detectives
			int votedoutindex=dovoting();
			totalalive--;
			curplayers.get(votedoutindex).die();
			System.out.println("Player"+curplayers.get(votedoutindex).getplayerno()+" has been voted out.");
		}
		System.out.println("--End of Round "+round+"--");
	}
	
	private int dovoting() {
		ArrayList<Player> voters=new ArrayList<Player>();
		int[] votes=new int[number];
		for(int i=0;i<number;i++) {
			votes[i]=0;
			if(curplayers.get(i).getstatus()) {
				voters.add(curplayers.get(i));//voters list only contains the alive people
			}
		}
		for(int i=0;i<voters.size();i++) {
			if(voters.get(i).equals(user)&&voters.get(i).getplayerno()==user.getplayerno()) {//if player is the user itself
				int votee=user.uservote()-1;
				while(votee>(number-1)||votee<0||!curplayers.get(votee).getstatus()) {
					System.out.print("Enter a valid choice:");
					votee=user.uservote()-1;
				}
				votes[votee]++;
			}
			else {//every other player than the user is a bot
				int votee=voters.get(i).botvote(voters)-1;
				votes[votee]++;
			}
		}
		int max=0;
		int maxi=0;
		for(int i=0;i<number;i++) {
			if(votes[i]>max) {
				maxi=i;
				max=votes[i];
			}
		}
		return maxi;
		
	}
	
	private void deathchecker() {
		boolean dead=false;
		for(int i=0;i<number;i++) {
			if(curplayers.get(i).getstatus() && !curplayers.get(i).equals(new Mafia(0)) && curplayers.get(i).gethealth()==0) {
				curplayers.get(i).die();
				totalalive--;
				dead=true;
				System.out.println("Player"+curplayers.get(i).getplayerno()+" has died.");
			}
		}
		if(!dead) {
			System.out.println("No one died.");
		}
	}
	
	private void mafiaschance() {
		int chosen=-1;
		
		if(user instanceof Mafia && user.getstatus()) {
			chosen=user.userplay();
			chosen--;//so as to use it as an index

			while(chosen<0||chosen>(number-1)||!curplayers.get(chosen).getstatus()||curplayers.get(chosen).equals(user)) {
				if(chosen<0||chosen>(number-1)) {
					System.out.print("Enter a valid player:");
					chosen=user.userplay();
					chosen--;
				}
				else if(!curplayers.get(chosen).getstatus()) {
					System.out.print("Enter an alive player:");
					chosen=user.userplay();
					chosen--;
				}
				else if(curplayers.get(chosen).equals(user)) {
					System.out.print("You cannot kill a mafia.");
					chosen=user.userplay();
					chosen--;
				}
			}
		}
		else {

			for(int i=0;i<curplayers.size();i++) {
				if(curplayers.get(i).getstatus() && curplayers.get(i).equals(new Mafia(0))) {
					chosen=curplayers.get(i).botplay(curplayers);
					chosen--;
					break;
				}
			}
			System.out.println("Mafias have chosen their target.");
		}

		if(chosen>=0){
			ArrayList<Player> mafiasthis=new ArrayList<Player>();
			int mdue=0;
			int mhpt=0;//mhpt stands for mafias health points total
			for(int i=0;i<curplayers.size();i++) {
				if(curplayers.get(i).getstatus() && curplayers.get(i).equals(new Mafia(0))) {//player is alive and a mafia
					mhpt+=curplayers.get(i).gethealth();
					mafiasthis.add(curplayers.get(i));
					mdue++;
				}
			}
			if(curplayers.get(chosen).gethealth()>=mhpt) {
				curplayers.get(chosen).sethealth(curplayers.get(chosen).gethealth()-mhpt);
				for(int i=0;i<curplayers.size();i++) {
					if(curplayers.get(i).getstatus()&&curplayers.get(i).equals(new Mafia(0))) {
						curplayers.get(i).sethealth(0);
					}
				}
			}
			else {//when mhpt>health of player to kill

				Collections.sort(mafiasthis);//Demonstration of object comparison is used.
				for(int i=0;i<mafiasthis.size();i++) {
					int plact=mafiasthis.get(i).getplayerno()-1;//(-1) to get the index
					int todeduct=curplayers.get(chosen).gethealth()/(mdue--);
					int deducted=Math.min(curplayers.get(plact).gethealth(),todeduct);//min is used so that health does not go negative
					curplayers.get(plact).sethealth(curplayers.get(plact).gethealth()-deducted);
					curplayers.get(chosen).sethealth(curplayers.get(chosen).gethealth()-deducted);

				}
			}
		}
	}
	
	private boolean detectiveschance() {
		int chosen=-1;
		boolean caught=false;
		
		if(user instanceof Detective && user.getstatus()) {
			chosen=user.userplay();
			chosen--;//so as to use it as an index

			while(chosen<0||chosen>(number-1)||!curplayers.get(chosen).getstatus()||curplayers.get(chosen).equals(user)) {
				if(chosen<0||chosen>(number-1)) {
					System.out.print("Enter a valid player:");
					chosen=user.userplay();
					chosen--;
				}
				else if(!curplayers.get(chosen).getstatus()) {
					System.out.print("Enter an alive player:");
					chosen=user.userplay();
					chosen--;
				}
				else if(curplayers.get(chosen).equals(user)) {
					System.out.print("You cannot test a detective.");
					chosen=user.userplay();
					chosen--;
				}
			}
			if(curplayers.get(chosen).equals(new Mafia(0))) {
				caught=true;
				 
			}
			if(caught) {
				System.out.println("Player"+(chosen+1)+" is a mafia.");
				lastcaughtmafiaindex=chosen;
				totalalive--;
				curplayers.get(lastcaughtmafiaindex).die();
			}
			else {
				System.out.println("Player"+(chosen+1)+" is not a mafia.");
			}
		}
		else {
			for(int i=0;i<curplayers.size();i++) {
				if(curplayers.get(i).getstatus() && curplayers.get(i).equals(new Detective(0))) {
					chosen=curplayers.get(i).botplay(curplayers);
					chosen--;
					break;
				}
			}
			if(chosen>=0&&curplayers.get(chosen).equals(new Mafia(0))) {
				caught=true;
				lastcaughtmafiaindex=chosen;
				totalalive--;
				curplayers.get(lastcaughtmafiaindex).die();
			}
			System.out.println("Detectives have chosen a player to test.");
		}
		return caught;
	}
	
	private void healerchance() {
		int totheal=0;
		for(int i=0;i<number;i++) {
			if(curplayers.get(i).getstatus()&&curplayers.get(i).equals(new Healer(0))) {
				totheal++;
			}
		}
		int chosen=-1;
		
		if(user instanceof Healer && user.getstatus()) {
			chosen=user.userplay();
			chosen--;//so as to use it as an index

			while(chosen<0||chosen>(number-1)||!curplayers.get(chosen).getstatus()) {
				if(chosen<0||chosen>(number-1)) {
					System.out.print("Enter a valid player:");
					chosen=user.userplay();
					chosen--;
				}
				else if(!curplayers.get(chosen).getstatus()) {
					System.out.print("Enter an alive player:");
					chosen=user.userplay();
					chosen--;
				}
			}
		}
		else {

			for(int i=0;i<curplayers.size();i++) {
				if(curplayers.get(i).getstatus() && curplayers.get(i).equals(new Healer(0))) {
					chosen=curplayers.get(i).botplay(curplayers);
					chosen--;
					break;
				}
			}
			System.out.println("Healers have chosen someone to heal.");
		}

		if(chosen>=0){
			curplayers.get(chosen).sethealth(curplayers.get(chosen).gethealth()+(500*totheal));
		}
	}
	
	private Player shuffler() {
		final Player user;
		final ArrayList<Mafia> allmafia=new ArrayList<Mafia>(mafias);
		final ArrayList<Detective> alldetective=new ArrayList<Detective>(detectives);
		final ArrayList<Healer> allhealer=new ArrayList<Healer>(healers);
		final ArrayList<Common> allcommoner=new ArrayList<Common>(commoners);
		Scanner in = new Scanner(System.in);
		System.out.println("Welcome to Mafia\n" + "Enter Number of players:");
		Random rand=new Random();
		number=in.nextInt();
		while(number<6) {
			System.out.println("Minimum players is 6:");
			number=in.nextInt();
		}
		totalalive=number;
		mafias=(int)(number/5);
		detectives=(int)(number/5);
		healers=Math.max(1, (int)(number/10));
		commoners=number-(mafias+detectives+healers);
		
		for(int i=1;i<=number;i++) {
			positions.add(i);
			allplayers.add(new Mafia(5));//just for initializing the array
		}
		
		for(int i=0;i<mafias;i++) {
			int pos=rand.nextInt(positions.size());
			int addat=positions.get(pos);
			positions.remove(pos);
			Player toadd=new Mafia(addat);
			allmafia.add((Mafia)toadd);
			allplayers.set(addat-1,toadd);
		}
		for(int i=0;i<detectives;i++) {
			int pos=rand.nextInt(positions.size());
			int addat=positions.get(pos);
			positions.remove(pos);
			Player toadd=new Detective(addat);
			alldetective.add((Detective)toadd);
			allplayers.set(addat-1,toadd);
		}
		for(int i=0;i<healers;i++) {
			int pos=rand.nextInt(positions.size());
			int addat=positions.get(pos);
			positions.remove(pos);
			Player toadd=new Healer(addat);
			allhealer.add((Healer)toadd);
			allplayers.set(addat-1,toadd);
		}
		for(int i=0;i<commoners;i++) {
			int pos=rand.nextInt(positions.size());
			int addat=positions.get(pos);
			positions.remove(pos);
			Player toadd=new Common(addat);
			allcommoner.add((Common)toadd);
			allplayers.set(addat-1,toadd);
		}
		
		System.out.println("Choose a Character\n" + 
				"1) Mafia\n" + 
				"2) Detective\n" + 
				"3) Healer\n" + 
				"4) Commoner\n" + 
				"5) Assign Randomly");
		int input=in.nextInt();
		while(input>5||input<1) {
			System.out.println("Enter a valid choice(int)");
			input=in.nextInt();
		}
		if(input==1) {
			System.out.print("You are Player"+allmafia.get(0).getplayerno()+".\r\n" + 
					"You are a mafia. Other maifas are:[");
			user=allmafia.get(0);
			for(int i=1;i<mafias;i++) {
				System.out.print("Player"+allmafia.get(i).getplayerno()+" ,");
			}
			System.out.println("]");
		}
		else if(input==2) {
			System.out.print("You are Player"+alldetective.get(0).getplayerno()+".\r\n" + 
					"You are a detective. Other detectives are:[");
			user=alldetective.get(0);
			for(int i=1;i<detectives;i++) {
				System.out.print("Player"+alldetective.get(i).getplayerno()+" ,");
			}
			System.out.println("]");
		}
		else if(input==3) {
			System.out.print("You are Player"+allhealer.get(0).getplayerno()+".\r\n" + 
					"You are a healer. Other healers are:[");
			user=allhealer.get(0);
			for(int i=1;i<healers;i++) {
				System.out.print("Player"+allhealer.get(i).getplayerno()+" ,");
			}
			System.out.println("]");
		}
		else if(input==4) {
			System.out.print("You are Player"+allcommoner.get(0).getplayerno()+".\r\n" + 
					"You are a commoner.");
			user=allcommoner.get(0);
		}
		else {//chosen random
			user=allplayers.get(0);
			System.out.println("You are Player"+user.getplayerno());
			if(user instanceof Mafia) {
				System.out.print("You are a mafia. Other maifas are:[");
				for(int i=0;i<mafias;i++) {
					if(allmafia.get(i).getplayerno()!=user.getplayerno()) {
					System.out.print("Player"+allmafia.get(i).getplayerno()+" ,");}
				}
				System.out.println("]");
			}
			else if(user instanceof Detective) {
				System.out.print("You are a detective. Other detectives are:[");
				for(int i=0;i<detectives;i++) {
					if(alldetective.get(i).getplayerno()!=user.getplayerno()) {
					System.out.print("Player"+alldetective.get(i).getplayerno()+" ,");}
				}
				System.out.println("]");
			}
			else if(user instanceof Healer) {
				System.out.print("You are a healer. Other healers are:[");
				for(int i=0;i<healers;i++) {
					if(allhealer.get(i).getplayerno()!=user.getplayerno()) {
					System.out.print("Player"+allhealer.get(i).getplayerno()+" ,");}
				}
				System.out.println("]");
			}
			else {
				System.out.println("You are a commoner.");
			}
			
		}
		for(int i=0;i<number;i++) {
			curplayers.add(allplayers.get(i));
		}
		return user;	
	}
}