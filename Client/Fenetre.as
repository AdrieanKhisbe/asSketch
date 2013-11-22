package  {
	import flash.display.MovieClip;
	import flash.display.Sprite;
	import flash.events.MouseEvent;
	import flash.events.KeyboardEvent;
	import flash.events.Event;
	/*Class Fenetre
		Class Contener qui contient toute les classes graphic et qui gere l'affichage principal des sous fenetres
	*/
	public class Fenetre extends Sprite{
		public var scene:MovieClip=InterfaceSock.scene;  
		public var iRC:IRC; //Fenetre de Dialogues
		public var reponse:Reponse; //Fenetre de Dialogues
		public var lesCo:LesCo; // Fenetre des connecté
		public var info:InfoPartie;
		public var dessin:Dessin;
		public var currentUser:User;
		public var buffClient:User;
		public var tabClients:Array = new Array(); //Tableau des Clients
		
		
		public function Fenetre() {
			this.afficherThis();
			this.instanceDialBoxs();
			this.instanceLesCo();
			this.instanceInfoP();
			this.instanceDessin();
			this.addNew(InterfaceSock.pseudo);
		}
		
		//Cree LE client 
		public function addNew(pseudo:String):void{
			this.currentUser = new User(pseudo);
			tabClients.push(currentUser);
			this.lesCo.addC(currentUser); // ajoute le pseudo dans la fenetre des connecté
		}
		
		
		//Cree un client extérieur
		public function addPre(pseudo:String):void{
			this. buffClient = new User(pseudo);
			tabClients.push(buffClient);
			this.lesCo.addC(buffClient); // ajoute le pseudo dans la fenetre des connecté
		}
		
		
		//Retir et suprime un joueur graphiquement et dans la méméoire
		public function removeClient(pseudo:String){
			var i:int = cherchePseudo(pseudo);
			this.lesCo.suppP(tabClients[i].pseudo); // retir le pseudo dans la fenetre des connecté
			tabClients[i]=null;
			tabClients.splice(i,1);
		}
		
		//Instanciation et affichage de la fenetre des connecté
		public function instanceLesCo(){
			lesCo = new LesCo(this);
			lesCo.afficherThis();
		}
		
		//Instanciation et affichage de la fenetre des connecté
		public function instanceDessin(){
			dessin = new Dessin(this);
			dessin.afficherThis();
		}
		
		//Instanciation et affichage de la fenetre des connecté
		public function instanceInfoP(){
			info = new InfoPartie(this);
			info.afficherThis();
		}
		
		//Instanciation et affichage de la fenetre de dialogue
		public function instanceDialBoxs(){
			iRC = new IRC(this);
			iRC.afficherThis();
			
			reponse = new Reponse(this);
			reponse.afficherThis();
		}
		
		//Affichage de la Fenetre
		public function afficherThis(){
			scene.addChild(this);
		}
		
		//Effacement de la Fenetre 
		public function effacerThis(){
			scene.removeChild(this);
			scene.mainFenetre=null;
		}
		
		
		public function cherchePseudo(pseudo:String){
			for(var i:int=0;i<tabClients.length;i++){
					if(tabClients[i].getPseudo() == pseudo){
						return i;
					}
			}
		}

	}
	
}