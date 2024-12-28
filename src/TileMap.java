import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * The TileMap class contains the data for a tile-based map, including Sprites.
 * Each tile is a reference to an Image. Of course, Images are used multiple
 * times in the tile map.
 */

public class TileMap {
    
    // game data
    public final int NUM_ROW = 18;
    public final int NUM_COL = 32;
    public final int TILE_SIZE = 60;
    
    // tile sprites
    private final Sprite CENTRE = (SpriteStore.get()).getSprite("sprites/tiles/centre.png");
    private final Sprite CHISELED = (SpriteStore.get()).getSprite("sprites/tiles/chiseled.png");
    private final Sprite FULL = (SpriteStore.get()).getSprite("sprites/tiles/full.png");
    private final Sprite HORIZONTAL = (SpriteStore.get()).getSprite("sprites/tiles/horizontal.png");
    private final Sprite VERTICAL = (SpriteStore.get()).getSprite("sprites/tiles/full.png");
    private final Sprite SWIRL = (SpriteStore.get()).getSprite("sprites/tiles/swirl.png");
    private final Sprite BROKEN_CROSS_L = (SpriteStore.get()).getSprite("sprites/tiles/brokenCrossL.png");
    private final Sprite BROKEN_CROSS_R = (SpriteStore.get()).getSprite("sprites/tiles/brokenCrossR.png");
    private final Sprite VINES_DANGLE = (SpriteStore.get()).getSprite("sprites/tiles/vinesDangle.png");
    private final Sprite HANGING_VINE_L = (SpriteStore.get()).getSprite("sprites/tiles/hangingVineL.png");
    private final Sprite HANGING_VINE_R = (SpriteStore.get()).getSprite("sprites/tiles/hangingVineR.png");
    private final Sprite LAVA = (SpriteStore.get()).getSprite("sprites/tiles/lava.png");
    private final Sprite LOCK = (SpriteStore.get()).getSprite("sprites/tiles/lock.png");
    private final Sprite EMPTY = (SpriteStore.get()).getSprite("sprites/tiles/empty.png");


    
    private int[][] tiles = new int[NUM_ROW][NUM_COL];
    private Game game;

    /**
	 * Creates a new TileMap with the specified width and height (in number of
	 * tiles) of the map.
	 */
	public TileMap(Game game, String level) {
		try {
		    loadMap(level);   
		} catch (Exception e) {
		    System.out.println(e);
		}
	    
	    this.game = game;
	} // constructor


    public int getSize() {
        return tiles.length;
    } // getSize

    public int getTile(int x, int y) {
        return tiles[y][x];
    } // getTile

    public void draw(Graphics g) {

        for (int y = 0; y < NUM_ROW; y++) {
            for (int x = 0; x < NUM_COL; x++) {
                switch (tiles[y][x]) {
                case 1:
                    HORIZONTAL.draw(g, x * TILE_SIZE, y * TILE_SIZE);
                    break;
                case 2:
                	HORIZONTAL.draw(g, x * TILE_SIZE, y * TILE_SIZE);
                    break;
                case 8:
                	LAVA.draw(g, x * TILE_SIZE, y * TILE_SIZE);
                	break;
                	
                case 7:
                	VERTICAL.draw(g, x * TILE_SIZE, y * TILE_SIZE);
                	break;
                case 3:
                	EMPTY.draw(g, x * TILE_SIZE, y * TILE_SIZE);
                	break;
                case 6:
                	LOCK.draw(g, x * TILE_SIZE, y * TILE_SIZE);
                case 9:
                    break;
                } // switch
            } // for
        } // for
    } // draw

    // static method which reads in the tile data from file

    public void loadMap(String fileName) throws IOException {
        ArrayList<String> lines = new ArrayList<>();
        
        try (InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream(fileName);
                InputStreamReader fr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(fr)) {
            for (String line = br.readLine(); line != null; line = br.readLine()) {
                // add every line except for comments
                if (!line.startsWith("#")) {
                    lines.add(line);
                } // if
            } // for            
            
            // parse the lines to create a TileEngine
            for (int y = 0; y < NUM_ROW; y++) {
                String[] line = lines.get(y).split(" ");
                for (int x = 0; x < NUM_COL; x++) {
                    int tileNum = Integer.parseInt(line[x]);
                    tiles[y][x] = tileNum;
                } // for
            } // for
        } // try
    } // loadMap

} // TileMap