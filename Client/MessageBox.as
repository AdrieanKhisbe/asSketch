﻿package  {
	
	import flash.display.MovieClip;
	import flash.events.MouseEvent;
	
	
	public class MessageBox extends MovieClip {
		var fenetre:Fenetre;
		
		public function MessageBox(fenetre:Fenetre,corps:String,titre:String) {
			this.fenetre = fenetre;
			this.x = 365;
			this.y = 325;
			this.titre.text = titre;
			this.corps.autoSize = "left";
			this.corps.text = corps;
			this.corps.background = false;
			this.corps.border = false;
			this.titre.background = false;
			this.titre.border = false;
						
			var add:int = this.corps.textHeight - 30;
			this.fond.height += add;
			this.ok.y += add;
			this.y -= add/2;
			
			fenetre.addChild(this);
			ok.addEventListener(MouseEvent.CLICK,close);
		}
		
		public function close(e:MouseEvent){
			fenetre.removeChild(this);
			ok.removeEventListener(MouseEvent.CLICK,close);                                                   
		}
		
	}
	
}
