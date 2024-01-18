import java.util.logging.Logger;
import java.util.logging.Level;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.catalyst.advanced.CatalystAdvancedIOHandler;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import uk.org.okapibarcode.backend.Code128;
import uk.org.okapibarcode.backend.Code3Of9;
import uk.org.okapibarcode.backend.HumanReadableLocation;
import uk.org.okapibarcode.output.Java2DRenderer;

public class BCGen implements CatalystAdvancedIOHandler {
	private static final Logger LOGGER = Logger.getLogger(BCGen.class.getName());
	
	@Override
    public void runner(HttpServletRequest req, HttpServletResponse res) throws Exception {

		try {
			
			//Code128
			String input = req.getParameter("message");
			if(input == null || input.trim().isEmpty()) {
				PrintWriter pw = res.getWriter();
				pw.write("{'error':'need input in the parameter \'message\''}");
				res.setContentType("application/json");//setting the content type
				return;
			}
			
			Code128 barcode = new Code128();
			//barcode.setFontName("Monospaced");
			barcode.setFontSize(0);
			barcode.setModuleWidth(1);
			barcode.setBarHeight(50);
			barcode.setHumanReadableLocation(HumanReadableLocation.NONE);
			barcode.setContent(input);
			
			int width = barcode.getWidth();
			int height = barcode.getHeight();

			BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
			Graphics2D g2d = image.createGraphics();
			Java2DRenderer renderer = new Java2DRenderer(g2d, 1, Color.WHITE, Color.BLACK);
			renderer.render(barcode);

			ImageIO.write(image, "png", res.getOutputStream());		
			
			res.setContentType("image/png");//setting the content type  
			
		}catch(Exception e) {
			PrintWriter pw = res.getWriter();
			pw.write("{'error':'unknown error'}");
			res.setContentType("application/json");//setting the content type
			return;
		}
	

	}
	
}