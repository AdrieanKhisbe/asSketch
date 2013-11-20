﻿package  {
	
	import flash.display.MovieClip;
	import flash.utils.Timer;
	import flash.events.TimerEvent;
	
	
	public class InfoPartie extends MovieClip {
		public var fenetre:Fenetre;
		private var start:Boolean = false;
		private var currentD:String = "Personne";
		private var prochainD:String = "Personne";
		private var motD:String = "";
		//timer
		private var minutes:uint = 3;
		private var secondes:uint = 0;
		private var total:uint = minutes*60 + secondes;
		private var myTimer:Timer = new Timer(1000, total);
		
		public function InfoPartie(f:Fenetre) {
			this.fenetre = f;
			this.x=500;
			this.y=600;
			this.cacheMot();
		}
		
		public function startT():void{
			this.vain.visible=false;
			minutes = 3;
			secondes = 0;
			myTimer.start();
			myTimer.addEventListener("timer", InitTimer);
			this.start=true;
		}
		
		public function vainqueur(pseudo:String){
			this.vain.text = "Vainqueur round : "+pseudo;
			this.vain.visible = true;
		}
		
		public function stopT():void{
			this.start=false;
			timer_txt.text = "Fin du round";
			myTimer.stop();
		}
		
		public function setCurrentD(user:String):void{
			this.currentD = user;
			this.des.text = currentD;
		}
		
		public function setProchainD(user:String):void{
			this.prochainD = user;
			this.pro.text = this.prochainD;
		}
		
		public function setMin(m:uint):void{
			this.minutes= m;
		}
		
		public function setSec(s:uint):void{
			this.secondes= s;
		}
		
		public function setMot(mot:String){
			this.motD = mot;
			this.mot.text = motD;
			this.mot.visible = true;
			this.text.visible = true;
		}
		
		public function cacheMot(){
			this.mot.visible = false;
			this.text.visible = false;
		}
		
		public function afficherThis(){
			fenetre.addChild(this);
		}
		
		public function effacerThis(){
			fenetre.removeChild(this);
		}
		
		
		public function InitTimer(ev:TimerEvent):void 
		{
			var timeLeft:uint = total-myTimer.currentCount
			var n:uint = (timeLeft%60);

			if (n < 10) 
			{
				timer_txt.text = uint((timeLeft/60))+" : 0"+n;
			} else {
				timer_txt.text = uint((timeLeft/60))+" : "+n;
			}
		}
		
	}
	
}
