﻿package 
			this.lanceCo();
			
			//Affichage correcte
			this.retour.visible = false;
			this.mGuest.visible = false;
			this.mInscription.visible = false;
			this.mInscrit.visible = false;
			
			this.iMenu.inscrit.addEventListener(MouseEvent.CLICK, clickInscrit);
			this.iMenu.spectateur.addEventListener(MouseEvent.CLICK, clickSpec);
			this.iMenu.inscription.addEventListener(MouseEvent.CLICK, clickInscrip);
			this.retour.addEventListener(MouseEvent.CLICK, clickRetour);
			this.mGuest.entrer.addEventListener(MouseEvent.CLICK, envoyerGuest);
			this.mGuest.pseudo.addEventListener(KeyboardEvent.KEY_DOWN, envoyerGuestK);
			this.mInscrit.entrer.addEventListener(MouseEvent.CLICK, envoyerInscrit);
			this.mInscrit.mdp.addEventListener(KeyboardEvent.KEY_DOWN, envoyerInscritK);
			this.mInscription.entrer.addEventListener(MouseEvent.CLICK, envoyerInscription);
			this.mInscription.mdp.addEventListener(KeyboardEvent.KEY_DOWN, envoyerInscriptionK);
			
			//Ajoute this dans le scénario principal
			this.iMenu.invite.removeEventListener(MouseEvent.CLICK, clickInvite);
			this.iMenu.inscrit.removeEventListener(MouseEvent.CLICK, clickInscrit);
			this.iMenu.spectateur.removeEventListener(MouseEvent.CLICK, clickSpec);
			this.iMenu.inscription.removeEventListener(MouseEvent.CLICK, clickInscrip);
			this.iMenu.retour.removeEventListener(MouseEvent.CLICK, clickRetour);
			this.retour.removeEventListener(MouseEvent.CLICK, clickRetour);
			this.mGuest.entrer.removeEventListener(MouseEvent.CLICK, envoyerGuest);
			this.mGuest.pseudo.removeEventListener(KeyboardEvent.KEY_DOWN, envoyerGuest);
			this.mInscrit.entrer.removeEventListener(MouseEvent.CLICK, envoyerInscrit);
			this.mInscrit.mdp.removeEventListener(MouseEvent.CLICK, envoyerInscrit);
			this.mInscription.entrer.removeEventListener(MouseEvent.CLICK, envoyerInscription);
			this.mInscription.mdp.removeEventListener(MouseEvent.CLICK, envoyerInscription);
		
		
		//Fonction d envois d'information au la connexin socket
		public function envoyerGuestK(e:KeyboardEvent):void
		{
			var pseudo:String = "";
			
			if (mGuest.pseudo.text != "" && e.keyCode == 13)
			{
				pseudo = mGuest.pseudo.text;
				//connexion.connexionGuest(pseudo);
				this.effacerThis();
			}
			/*
			else if (mGuest.pseudo.text == "" )
			{
				setError("Pseudo manquant");
			}
			*/
		}
		public function envoyerGuest(e:MouseEvent):void
		{
			var pseudo:String = "";
			
			if (mGuest.pseudo.text != "" )
			{
				pseudo = mGuest.pseudo.text;
				//connexion.connexionGuest(pseudo);
				scene.gotoAndPlay(2);
				this.effacerThis();
			}
			/*
			else if (mGuest.pseudo.text == "" )
			{
				setError("Pseudo manquant");
			}
			*/
		}
		public function envoyerInscritK(e:KeyboardEvent):void
		{
			var pseudo:String = "";
			var mdp:String = "";
			
			if (mInscrit.pseudo.text != "" && mInscrit.mdp.text != "" && e.keyCode == 13)
			{
				pseudo = mInscrit.pseudo.text;
				mdp = mInscrit.mdp.text;
				this.effacerThis();
			}
			/*
			else if (mGuest.pseudo.text == "" )
			{
				setError("Pseudo manquant");
			}
			*/
		}
		public function envoyerInscrit(e:MouseEvent):void
		{
			var pseudo:String = "";
			var mdp:String = "";
			
			if (mInscrit.pseudo.text != "" && mInscrit.mdp.text != "" )
			{
				pseudo = mInscrit.pseudo.text;
				mdp = mInscrit.mdp.text;
				this.effacerThis();
			}
			/*
			else if (mGuest.pseudo.text == "" )
			{
				setError("Pseudo manquant");
			}
			*/
		}
		public function envoyerInscription(e:MouseEvent):void
		{
			var pseudo:String = "";
			var mdp:String = "";
			
			if (mInscription.pseudo.text != "" && mInscription.mdp.text != "" )
			{
				pseudo = mInscription.pseudo.text;
				mdp = mInscription.mdp.text;
				this.effacerThis();
			}
			/*
			else if (mGuest.pseudo.text == "" )
			{
				setError("Pseudo manquant");
			}
			*/
		}
		public function envoyerInscriptionK(e:KeyboardEvent):void
		{
			var pseudo:String = "";
			var mdp:String = "";
			
			if (mInscription.pseudo.text != "" && mInscription.mdp.text != "" && e.keyCode == 13)
			{
				pseudo = mInscription.pseudo.text;
				mdp = mInscription.mdp.text;
				this.effacerThis();
			}
			/*
			else if (mGuest.pseudo.text == "" )
			{
				setError("Pseudo manquant");
			}
			*/
		}
		
		//Fonction de cick sur les bouttons du Menu
		{
			this.retour.visible = false;
			this.mGuest.visible = false;
			this.mInscription.visible = false;
			this.mInscrit.visible = false;
			this.iMenu.visible = true;
			this.mGuest.pseudo.text = "";
			this.mInscrit.pseudo.text = "";
			this.mInscrit.mdr.text = "";
			this.mInscription.pseudo.text = "";
			this.mInscription.mdp.text = "";
		}
			this.retour.visible = true;
			this.mGuest.visible = true;
		public function clickInscrit(e:MouseEvent):void
		{
			this.iMenu.visible = false;
			this.mInscrit.visible = true;
			this.mInscrit.mdp.displayAsPassword=true;
			this.retour.visible = true;
		}
		public function clickSpec(e:MouseEvent):void
		{
			this.iMenu.visible = false;
			this.retour.visible = true;
		}
		public function clickInscrip(e:MouseEvent):void
		{
			this.iMenu.visible = false;
			this.mInscription.visible = true;
			this.mInscription.mdp.displayAsPassword=true;
			this.retour.visible = true;
		}
		