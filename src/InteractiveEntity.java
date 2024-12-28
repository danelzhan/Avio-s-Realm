public class InteractiveEntity extends Entity {

	private String [] dialogue = {"dialogue error"};
	private boolean talkingWith = false;
	private boolean talkedWith = false;
	private int prevDialogue = 0;
	private boolean attacked;
	private boolean skip = false;
	
	Game game;

	public InteractiveEntity(Game g, String r, int newX, int newY, String [] dialogue) {

		super(r, newX, newY);
		this.game = g;
		this.dialogue = dialogue;

	} // constructor

    public String [] getDialogue () {
    	return dialogue;
    }
    
    public void checkReset () {
    	if (game.resetInteractives) {
    		this.talkedWith = false;
    		game.resetInteractives = false;
    		prevDialogue = 0;
    		
    	} // if
    } // checkReset
	
	public void setTalkedWith(boolean b) {
		this.talkedWith = b;
	}

	public void setTalkingWith(boolean b) {
		this.talkingWith = b;
	} // setTalkingWith

	@Override
	public void collidedWith(Entity other) {
		
		String emptyDialogue = "";
		
		if (game.talkPressed && !talkedWith) {
			game.playSoundEffect((int)(Math.random() * (28 - 27) + 27));
			game.displayMessage = true;
			game.talkMessage = this.dialogue[0];
			
			talkedWith = true;
			
		} // if
		
		if (game.skipPressed) {
			try {
				game.talkMessage = this.dialogue[prevDialogue + 1];
				prevDialogue++;
				game.skipPressed = false;
			} catch (Exception e) {
				game.talkMessage = emptyDialogue;
				game.resetInteractives = true;
				game.skipPressed = false;
			} // catch
		} // if
		
	} // collidedWith
	
} // InteractiveEntity