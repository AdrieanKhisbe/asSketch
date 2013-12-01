package  {
	
	import flash.display.MovieClip;
	import flash.events.MouseEvent;
	
	
	public class MenuOption extends MovieClip {
		public var option:Options
		
		public function MenuOption(opt:Options) {
			this.option=opt;
			this.x = -170;
			this.y =  50;
		}
		
		public function afficherThis(){
			option.addChild(this);
		}
		
		public function effacerThis(){
			option.removeChild(this);
			//fenetre.this.addEventListener(MouseEvent.CLICK,openMenu);
		}
	}
	
}
