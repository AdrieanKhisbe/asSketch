package  {
	
	import flash.display.MovieClip;
	
	
	public class InterfaceSock extends MovieClip {
		public static var scene:MovieClip;
		public static var fenetre:Fenetre;
		public static var connexion:ConnexionSocket;
		public static var acceuil:AccueilPage;
		public static var pseudo:String;
		
		public function InterfaceSock(s:MovieClip,c:ConnexionSocket,a:AccueilPage,f:Fenetre) {
			scene=s;
			fenetre=f;
			connexion=c;
			acceuil=a;
		}
		
		
		
		//Fonctions  client -> Serveur
		//OK
		public static function connexionGuest(pseudoo:String)
		{
			
			pseudo=pseudoo;
			connexion.sendText("CONNECT/" + escape(pseudo));
			scene.gotoAndPlay(2);
		}
		public static function deconnexionGuest(pseudo:String)
		{
			connexion.sendText("EXIT/" + escape(pseudo));
		}
		//OK
		public static function motClient(mot:String)
		{
			connexion.sendText("GUESS/" + escape(mot));
		}
		public static function changeCouleur(r:int ,g:int ,b:int)
		{
			connexion.sendText("SET_COLOR/" + r +"/"+ g +"/"+ b);
		}
		public static function tailleTrait(s:int)
		{
			connexion.sendText("SET_SIZE/" + s);
		}
		public static function traceTrait(x1:int,y1:int,x2:int,y2:int)
		{
			connexion.sendText("SET_SIZE/" + x1 +"/"+ y1 +"/"+ x2 +"/"+ y2);
		}
		
		//Fonction Serveur->Client
		
		public static function clientCo(user:String){
			fenetre.addPre(user);
		}
		
		public static function clientDeco(user:String){
			fenetre.removeClient(user);
			
		}
		public static function role(user:String,mot:String){
			if(mot == "0"){
				fenetre.info.startT();
				fenetre.info.cacheMot();
			}else{
				fenetre.info.startT();
				fenetre.currentUser.setDes();
				fenetre.info.setMot(mot);
			}
		}
		public static function reponseC(mot:String,user:String){
			fenetre.reponse.addT(user+ " : " + mot);
		}
		public static function trouveC(user:String){
			fenetre.reponse.addT("***** " + user +" a trouvé le mot !");
		}
		public static function enclancheT(time:String){
			fenetre.info.setMin(0);
			fenetre.info.setSec(uint(time));
		}
		public static function scoreC(user:String,score:String){
			for(var i:int; i< fenetre.lesCo.pseudo.length ; i++){
				if(fenetre.lesCo.pseudo[i].getPseudo() == user ){
					fenetre.lesCo.pseudo[i].setPoints(int(score));
					break;
				}
			}
			fenetre.lesCo.refrechP();
			
		}
		public static function finiR(user:String,mot:String){
			fenetre.info.stopT();
			fenetre.info.setMot(mot);
			fenetre.info.vainqueur(user);
		}
		
		
	}
	
}
