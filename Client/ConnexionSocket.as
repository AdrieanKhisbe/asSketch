﻿package 
		// port du serveur
		private var port:Number;
		//ip du serveur
		private var host:String;
			this.host = host;
			this.port = port;
			
			/*
			try{
				connexion.connect(host,port);
			}catch(error:Error){
				scene.gotoAndStop(1);
			}
			*/
		

		
		public function getPseudo():String
		{
			return pseudo;
		}
			
				//CONNECTED/user/
				//EXITED/user/
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
			}
		
		
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
		
		