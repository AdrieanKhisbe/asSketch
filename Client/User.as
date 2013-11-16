package  {	import flash.display.MovieClip;	/*Class User	*/	public class User extends MovieClip{		private var pseudo:String;
		private var points:int = 0;
		private var dessinateur:Boolean = false;				public function User(pseudo:String) {			this.pseudo=pseudo;		}
		
		public function getPseudo():String {
			return pseudo;
		}
		
		public function getPoints():int {
			return points;
		}
		
		public function setPoints(points:int):void {
			this.points = points;
		}
		
		public function setDes():void{
			this.dessinateur = true;
		}
		
		public function setNDes():void{
			this.dessinateur = false;
		}
		
		public function getDes():Boolean{
			return this.dessinateur;
		}	}	}