import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Panel;



@SuppressWarnings("serial")
public class ImagePanel extends Panel {
	private Image myImage;

	public ImagePanel( Image i ) {
		MediaTracker tracker = new MediaTracker( this );
		tracker.addImage( i, 0 );
		try {
			tracker.waitForAll();
		}
		catch( InterruptedException x ) {
			System.err.println( "MediaTracker interrupted!" );
		}
		myImage = i;
	}

	public Dimension getPreferredSize() {
		return( new Dimension( myImage.getWidth( this ),
				myImage.getHeight( this ) ) );
	}

	public void paint( Graphics g ) {
		g.drawImage( myImage, 0, 0, this );
	}

	public Image getImage() {
		return( myImage );
	}

	public void setImage( Image i ) {
		myImage = i;
	}
}