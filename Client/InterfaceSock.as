package  {
	import flash.display.MovieClip;
	import flash.utils.*;
	
	
	
	public class InterfaceSock extends MovieClip {
		public static var scene:MovieClip;
		public static var pseudo:String;
		public static var x1:int;
		public static var y1:int;
		public static var xB:int;
		public static var yB:int;
		
		public function InterfaceSock(s:MovieClip) {
			scene=s;
		}
		
		
		
		//Fonctions  client -> Serveur
		//OK
		public static function connexionGuest(pseudoo:String)
		{
			pseudo=pseudoo;
			scene.connexion.sendText("CONNECT/" + escape(pseudo));
			scene.gotoAndPlay(2);
		}
		public static function deconnexionGuest(pseudo:String)
		{
			scene.connexion.sendText("EXIT/" + escape(pseudo));
		}
		//OK
		public static function motClient(mot:String)
		{
			scene.connexion.sendText("GUESS/" + escape(mot));
		}
		public static function changeCouleur(r:int ,g:int ,b:int)
		{
			scene.connexion.sendText("SET_COLOR/" + r +"/"+ g +"/"+ b);
		}
		public static function tailleTrait(s:int)
		{
			scene.connexion.sendText("SET_SIZE/" + s);
		}
		public static function traceTrait(x2:int,y2:int)
		{
			//scene.connexion.sendText("SET_LINE/" + x2 +"/"+ y2 +"/"+ x2 +"/"+ y2);
			if(x1 != -1 && y1 != -1){
				xB = x2;
				yB = y2;
				scene.connexion.sendText("SET_LINE/" + x1 +"/"+ y1 +"/"+ x2 +"/"+ y2);
				x1 = -1 ;
				y1 = -1 ;
			}else{
				x1= x2;
				y1 = y2;
				scene.connexion.sendText("SET_LINE/" + xB +"/"+ yB +"/"+ x2 +"/"+ y2);
			}
		}
		
		//Fonction Serveur->Client
		
		public static function clientCo(user:String){
			scene.mainFenetre.addPre(user);
		}
		
		public static function clientDeco(user:String){
			scene.fenetre.removeClient(user);
			
		}
		public static function role(user:String,mot:String){
			if(user == "chercheur"){
				scene.mainFenetre.info.startT();
				scene.mainFenetre.info.setCurrentD(mot);
				scene.mainFenetre.info.cacheMot();
				
			}else{
				scene.mainFenetre.info.startT();
				scene.mainFenetre.currentUser.setDes();
				scene.mainFenetre.info.setCurrentD(pseudo);
				scene.mainFenetre.info.setMot(mot);
			}
		}
		public static function reponseC(mot:String,user:String){
			scene.mainFenetre.reponse.addT(mot+ " : " + user);
		}
		public static function trouveC(user:String){
			scene.mainFenetre.reponse.addT("***** " + user +" a trouvé le mot !");
		}
		public static function enclancheT(time:String){
			scene.mainFenetre.info.setMin(0);
			scene.mainFenetre.info.setSec(uint(time));
		}
		public static function scoreC(user:String,score:String){
			for(var i:int; i< scene.mainFenetre.lesCo.pseudo.length ; i++){
				if(scene.mainFenetre.lesCo.pseudo[i].getPseudo() == user ){
					scene.mainFenetre.lesCo.pseudo[i].setPoints(int(score));
					break;
				}
			}
			scene.mainFenetre.lesCo.refrechP();
			
		}
		public static function finiR(user:String,mot:String){
			scene.mainFenetre.info.stopT();
			scene.mainFenetre.info.setMot(mot);
			scene.mainFenetre.info.vainqueur(user);
		}
		
		static function sleep(ms:int):void {
			var init:int = getTimer();
			while(true) {
				if(getTimer() - init >= ms) {
					break;
				}
			}
		}
		
		static function drawL(x1:int,x2:int,y1:int,y2:int){
			scene.mainFenetre.dessin.dessinExt(x1,y1,x2,y2);
		}
		
		
	}
	
}
