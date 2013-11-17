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
		public var scene:MovieClip;  
		public var dialBox:DialogueBox; //Fenetre de Dialogues
		public var lesCo:LesCo; // Fenetre des connecté
		public var pseudo:String;
		public var currentUser:User;
		public var buffClient:User;
		public var tabClients:Array = new Array(); //Tableau des Clients
		
		
		public function Fenetre(scene:MovieClip) {
			this.scene=scene;
			this.afficherThis();
			this.instanceDialBox();
			this.instanceLesCo();
			//scene.connexion.getPseudo()
			this.addNew("Pene");
		}
		
		//Cree LE client 
		public function addNew(pseudo:String):void{
			this.currentUser = new User(pseudo);
			tabClients.push(currentUser);
			this.lesCo.addP(pseudo); // ajoute le pseudo dans la fenetre des connecté
		}
		
		
		//Cree un client extérieur
		public function addPre(pseudo:String):void{
			this. buffClient = new User(pseudo);
			tabClients.push(buffClient);
			this.lesCo.addP(pseudo); // ajoute le pseudo dans la fenetre des connecté
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
		
		//Instanciation et affichage de la fenetre de dialogue
		public function instanceDialBox(){
			dialBox = new DialogueBox(this);
			dialBox.afficherThis();
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