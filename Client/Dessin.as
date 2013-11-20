package  {
	
	import flash.display.Sprite
	import flash.text.TextField;
	import flash.events.MouseEvent;
	import flash.events.Event;
	import flash.events.KeyboardEvent;
	import flash.display.BitmapData;
	import flash.display.Bitmap;
	import flash.display.MovieClip;
	
	public class Dessin extends MovieClip {
		var pal:Object;
		var DRAW:Object;
		var taille:int = 5;
		var fenetre:Fenetre;
		
		public function Dessin(fenetre:Fenetre) {
			fenetre.debug.text = "a";
			this.fenetre=fenetre;
			fenetre.debug.text = "b";
		}
		
		public function afficherThis(){
			fenetre.debug.text = "c";
			fenetre.addChild(this);
			fenetre.debug.text = "d";
			drawer(675,600, 0.5);
			PALETTE(2);
			fenetre.debug.text = "fina";
		}
		
		public function effacerThis(){
			fenetre.removeChild(this);
			fenetre.dessin=null;
		}
		
		
		function drawer(SizeX:int , SizeY:int ,alph:Number=.1 ,colors:int=0xFFFFFF ):void {	
		   DRAW={  size:1, fsize:taille, fmax:40, multy:0.5,
						color:0x000000, zone:new Sprite(), bg:new carre(SizeX,SizeY,colors,alph), graph:new Sprite(), M:new carre(SizeX,SizeY,0x000000),
						GL:new Sprite(),
						B:new cbutton('X') ,br1:new cbutton('+'),br2:new cbutton('o'),br3:new cbutton('-'), col:colors, A:alph  };
		   with(DRAW){
			addChild(zone); zone.x=75; zone.y=0; 
			zone.addChild(bg);
			zone.addChild(graph);
			zone.addChild(M);
			graph.mask=M;
			zone.addChild(GL);GL.y=10; GL.x=-50;
			GL.addChild(B);B.x=0; B.y=0;
			GL.addChild(br1);br1.y=50;
			GL.addChild(br2);br2.y=100;
			GL.addChild(br3);br3.y=150;
			
			   
				graph.graphics.lineTo(20,20);
			   
			br1.addEventListener(MouseEvent.CLICK,brushSizeMax);
			br2.addEventListener(MouseEvent.CLICK,brushSize);
			br3.addEventListener(MouseEvent.CLICK,brushSizeMin);
			graph.mouseEnabled=false;
			zone.addEventListener(MouseEvent.MOUSE_DOWN,handleMouseDown);
			zone.addEventListener(MouseEvent.MOUSE_UP,handleMouseUp);
			B.addEventListener(MouseEvent.CLICK,deleteStage);
		  }
		}
		
		public function PALETTE(Type:uint, Tx:int=115, Ty:int=115):void
		{
			
			pal={clip:new MovieClip(), p:new carre(30,30,0xFFFFFF,1) ,o:new cbutton('P'),C:new curs(),
						 tx:75, ty:75
						 }
			
			 with(pal){
				type=Type;
				this.addChild(clip);clip.x=0;clip.y=525;
				clip.addChild(p);p.y=-35;p.x=21;
				clip.addChild(o);o.y=-60;o.x=26;
				o.addEventListener('click',change_palette)
				var B:Sprite;
			 }
			 switch( Type ){
				case 1:
					var nRadius:Number =pal.tx>>1;
					var R:PALrond = new PALrond(nRadius);
					B=new Sprite();
					B.addChild(R)
					R.x = R.y = nRadius;
					vectorTObitmap(B);
				break; 
				case 2:
					var N:PALcarre = new PALcarre(pal.tx, pal.ty);
					B=new Sprite();
					B.addChild(N);
					vectorTObitmap(B);
				break; 
				}
				
		}
		function vectorTObitmap(map):void {  
			pal['bmp']=new BitmapData(pal.tx, pal.ty, false, 0x000000);
			pal['T'] =new Bitmap();
			pal['BMP'] = new carre(pal.tx,pal.ty,0x00,1);
			with(pal){
				bmp.draw(map,null,null,null,null,true);
				T=new Bitmap(bmp);
				clip.addChild(BMP);
				clip.addChild(T);
				clip.mouseEnabled=false;
				clip.addChild(C)
				BMP.addEventListener('click',Getcolor)
				}
		}
		function Getcolor(e):void {  
		var pixelValue:uint = pal.bmp.getPixel(pal.T.mouseX, pal.T.mouseY);
		var color='0x'+pixelValue.toString(16); 
		DRAW.color=color;
		pal.C.x=pal.T.mouseX;
		pal.C.y=pal.T.mouseY;
		TweenMax.to(pal.p, .3, {colorMatrixFilter:{colorize:color}});

		}
		function change_palette(map):void { 
			with(pal){
				del_palette();
				if(type==1)PALETTE(2)
				else PALETTE(1)
				}
		}
		function del_palette():void {  
			with(pal){
				clip.removeEventListener('click',Getcolor);
				clip.removeChild(T);
				removeChild(clip);
			}
		}
		
		function handleMouseDown(event:MouseEvent):void {	
		  with(DRAW){size=fsize;
			//size  = 1;
			//graph.graphics.lineStyle(size,color);
			graph.graphics.lineStyle(undefined);
			graph.graphics.moveTo(zone.mouseX,zone.mouseY);
			zone.addEventListener(MouseEvent.MOUSE_MOVE,startDrawing);
			zone.addEventListener(MouseEvent.MOUSE_OUT,handleMouseUp);
			TweenMax.to(DRAW, 1, {  onUpdate:upSize});
		}}
		function handleMouseUp(event:MouseEvent):void {
			TweenMax.to(DRAW, 0, {});
			DRAW.zone.removeEventListener(MouseEvent.MOUSE_MOVE,startDrawing);
		}
		function startDrawing(event:MouseEvent):void {	
			 with(DRAW){
				graph.graphics.lineTo(zone.mouseX,zone.mouseY);
			}}
		function deleteStage(event:MouseEvent):void {
			with(DRAW)graph.graphics.clear();
		}
		function upSize():void {
		   with(DRAW){
			if(size<=fmax+.3)
			graph.graphics.lineStyle(size,color);
			}}
		function brushSize(event:MouseEvent):void {
			with(DRAW){fsize=5; fmax=40; taille =5;};
		}
		function brushSizeMin(event:MouseEvent):void {
			with(DRAW){
				if(taille == 1){
					fsize=taille; fmax=5;
				}else{
					fsize=taille--; fmax=5;
				}
			};
		}
		function brushSizeMax(event:MouseEvent):void {
			with(DRAW){
				if(taille == 25){
					fsize=taille; fmax=100;
				}else{
					fsize=taille++; fmax=100;
				}				
			};
		}
		
		
	}
	
}