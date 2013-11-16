package {	import flash.display.Sprite;	import flash.display.MovieClip;	import flash.net.XMLSocket;	import flash.display.MovieClip;	import fl.motion.Color;	import flash.events.IOErrorEvent;	import flash.events.Event;	import flash.events.DataEvent;	import flash.events.MouseEvent;	import flash.events.IOErrorEvent;	import flash.events.SecurityErrorEvent;	import flash.events.UncaughtErrorEvents;	public class ConnexionSocket	{		//Variable permettant de savoir si le client est connecté		private var bConnected:Boolean;		//Pour cibler vers la scène accecible uniquement dans le paktage		internal var scene:MovieClip;		//Instance de la connection		private var connexion:XMLSocket = new XMLSocket();		// Id du client		private var idClient:Number;		//pseudo		private var pseudo:String;
		// port du serveur
		private var port:Number;
		//ip du serveur
		private var host:String;		//Constructeur :		public function ConnexionSocket(scene :MovieClip,host:String,port:Number)		{
			this.host = host;
			this.port = port;
						//connexion.addEventListener(SecurityErrorEvent.SECURITY_ERROR, connexionN2);			//Connexion non etablie			connexion.addEventListener(IOErrorEvent.IO_ERROR, connexionN1);			//CONECTION AU SOCKET REUSSI			connexion.addEventListener(Event.CONNECT, connexionR);			//Conection au socket interrompu;			connexion.addEventListener(Event.CLOSE, connexionC);			//Reception de donné;			connexion.addEventListener(DataEvent.DATA, receiveData);			//On stoque la référence de la scène :;			this.scene = scene;			//On établie la conection au server
			/*
			try{
				connexion.connect(host,port);
			}catch(error:Error){
				scene.gotoAndStop(1);
			}
			*/		}
		
		public function getCo():XMLSocket		{			return connexion;		}
		
		public function getPseudo():String
		{
			return pseudo;
		}		//Affcihe sur le client lorsque la conexion est établie		public function connexionR(Evt:Event):void		{					}		//Affiche sur le client lorsque la conexion est perdu		public function connexionC(Evt:Event):void		{				scene.gotoAndStop(1);				//scene.accueil.setError("Le Serveur vien d'etre couper , veuillez pacienter quelques minute avant de vous reconnecté");				scene.mainFenetre.effacerThis();				scene.accueil.afficherThis();		}				public function connexionN1(e:IOErrorEvent):void		{				scene.gotoAndStop(1);				//scene.accueil.setError("Serveur Off-Line");				scene.mainFenetre.effacerThis();				scene.accueil.afficherThis();		}				public function connexionN2(e:SecurityErrorEvent):void		{				scene.gotoAndStop(1);				//scene.accueil.setError("Serveur Off-Line");				scene.mainFenetre.effacerThis();				scene.accueil.afficherThis();		}		//Traitement de l'information lors de reçois d'un message du serveur		public function receiveData(Evt:DataEvent)		{
						var tabData:Array = Evt.data.split("/");			if (tabData[0] == "CONNECTED")			{
				//CONNECTED/user/			}			else if (tabData[0] == "EXITED")			{
				//EXITED/user/			}
			else if (tabData[0] == "NEW_ROUND")
			{
				//NEW_ROUND/role/mot/
			}
			else if (tabData[0] == "GUESSED")
			{
				//GUESSED/mot/user/
			}
			else if (tabData[0] == "WORD_FOUND")
			{
				//WORD_FOUND/joueur/
			}
			else if (tabData[0] == "WORD_FOUND_TIMEOUT")
			{
				//WORD_FOUND_TIMEOUT/timeout/
			}
			else if (tabData[0] == "END_ROUND")
			{
				//END_ROUND/user/mot/
			}
			else if (tabData[0] == "SCORE_ROUND")
			{
				//SCORE_ROUND/user1/score1/users2/score2/.../userN/scoreN/
			}
			else if (tabData[0] == "END_ROUND")
			{
				//END_ROUND/user/mot/
			}
			else if (tabData[0] == "LINE")
			{
				//LINE/x1/y1/x2/y2/r/g/b/s/
			}
			else if (tabData[0] == "ERROR")
			{
				//ERROR/x
			}		}
		
		
		//Fonctions  client -> Serveur
		public function connexionGuest(pseudo:String)
		{
			this.sendText("CONNECT/" + escape(pseudo));
			scene.gotoAndPlay(2);
		}
		public function deconnexionGuest(pseudo:String)
		{
			this.sendText("EXIT/" + escape(pseudo));
		}
		public function motClient(mot:String)
		{
			this.sendText("GUESS/" + escape(mot));
		}
		public function changeCouleur(r:int ,g:int ,b:int)
		{
			this.sendText("SET_COLOR/" + r +"/"+ g +"/"+ b);
		}
		public function tailleTrait(s:int)
		{
			this.sendText("SET_SIZE/" + s);
		}
		public function traceTrait(x1:int,y1:int,x2:int,y2:int)
		{
			this.sendText("SET_SIZE/" + x1 +"/"+ y1 +"/"+ x2 +"/"+ y2);
		}
		
				//converti le message a envoyer en XML		public function sendText(sText:String)		{			var xText:XML = new XML(sText);			sendXML(xText);		}		// envois au server un message		public function sendXML(xData:XML)		{			connexion.send(xData);		}	}}