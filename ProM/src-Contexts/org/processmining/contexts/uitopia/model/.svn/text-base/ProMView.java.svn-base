package org.processmining.contexts.uitopia.model;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.deckfour.uitopia.api.model.Resource;
import org.deckfour.uitopia.api.model.View;
import org.deckfour.uitopia.api.model.ViewType;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.hub.ProMViewManager;
import org.processmining.contexts.uitopia.hub.overlay.ProgressOverlayDialog;
import org.processmining.framework.plugin.PluginExecutionResult;
import org.processmining.framework.plugin.PluginParameterBinding;
import org.processmining.framework.util.Pair;

public class ProMView implements View {

	private final JPanel component;
	private final ProMViewManager manager;
	private String name;
	private final ProMResource<?> resource;
	protected static GraphicsConfiguration gc;
	private final ProMViewType type;
	private BufferedImage original;
	private BufferedImage scaledImage;
	private final Pair<Integer, PluginParameterBinding> binding;
	private boolean working = true;

	public ProMView(ProMViewManager manager, ProMViewType type, ProMResource<?> resource, String name,
			Pair<Integer, PluginParameterBinding> binding) {
		this.manager = manager;
		this.type = type;
		this.resource = resource;
		this.binding = binding;
		resource.setView(this);
		this.name = name;
		component = new JPanel(new BorderLayout());
		component.setBorder(BorderFactory.createEmptyBorder());
		component.setOpaque(false);
		original = toBufferedImage(resource.getType().getTypeIcon());

		refresh(0);
	}

	public void destroy() {
		component.removeAll();
	}

	public String getCustomName() {
		return name;
	}

	public Image getPreview(int maxWidth, int maxHeight) {
		synchronized (original) {

			int originalWidth = original.getWidth();
			int originalHeight = original.getHeight();
			double scaleFactor = (double) maxWidth / (double) originalWidth;
			double scaleY = (double) maxHeight / (double) originalHeight;
			if (scaleY < scaleFactor) {
				scaleFactor = scaleY;
			}

			int scaledWidth = Math.max(1, (int) (originalWidth * scaleFactor));
			int scaledHeight = Math.max(1, (int) (originalHeight * scaleFactor));

			scaledImage = createCompatibleImage(scaledWidth, scaledHeight);
			Graphics2D g2ds = scaledImage.createGraphics();
			g2ds = scaledImage.createGraphics();
			g2ds.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			g2ds.drawImage(original, 0, 0, scaledWidth, scaledHeight, null);
			g2ds.dispose();
			return scaledImage;
		}

	}

	public Resource getResource() {
		return resource;
	}

	public JComponent getViewComponent() {
		return component;
	}

	public void setCustomName(String name) {
		this.name = name;
	}

	private BufferedImage createCompatibleImage(int width, int height) {
		if (gc == null) {
			gc = component.getGraphicsConfiguration();
			if (gc == null) {
				gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice()
						.getDefaultConfiguration();
			}
		}
		return gc.createCompatibleImage(width, height, Transparency.TRANSLUCENT);
	}

	public ViewType getType() {
		return type;
	}

	public void captureNow() {
		// Record a screen-capture of the currenly visible frame
		synchronized (original) {
			Dimension size = component.getSize();

			original = createCompatibleImage(size.width, size.height);
			Graphics2D g2d = original.createGraphics();
			component.paint(g2d);
			g2d.dispose();

		}
	}

	// This method returns a buffered image with the contents of an image
	private BufferedImage toBufferedImage(Image image) {
		if (image instanceof BufferedImage) {
			return (BufferedImage) image;
		}

		// This code ensures that all the pixels in the image are loaded
		image = new ImageIcon(image).getImage();

		// Create a buffered image with a format that's compatible with the screen
		BufferedImage bimage = null;
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		try {
			// Determine the type of transparency of the new buffered image
			int transparency = Transparency.OPAQUE;

			// Create the buffered image
			GraphicsDevice gs = ge.getDefaultScreenDevice();
			GraphicsConfiguration gc = gs.getDefaultConfiguration();
			bimage = gc.createCompatibleImage(image.getWidth(null), image.getHeight(null), transparency);
		} catch (HeadlessException e) {
			// The system does not have a screen
		}

		if (bimage == null) {
			// Create a buffered image using the default color model
			int type = BufferedImage.TYPE_INT_RGB;
			bimage = new BufferedImage(image.getWidth(null), image.getHeight(null), type);
		}

		// Copy image to buffered image
		Graphics g = bimage.createGraphics();

		// Paint the image onto the buffered image
		g.drawImage(image, 0, 0, null);
		g.dispose();

		return bimage;
	}

	public void refresh() {
		refresh(0);
	}

	public void refresh(final int millisToPopup) {

		final UIPluginContext context = manager.getContext().getMainPluginContext()
				.createChildContext("Visualizing: " + resource.getName());
		context.getParentContext().getPluginLifeCycleEventListeners().firePluginCreated(context);

		final ProgressOverlayDialog dialog = new ProgressOverlayDialog(manager.getContext().getController()
				.getMainView(), context, "Please wait while updating visualization...");
		dialog.setIndeterminate(false);

		Thread thread = new Thread(new Runnable() {

			public void run() {
				manager.getContext().getController().getMainView().showOverlay(dialog);
				synchronized (ProMView.this) {
					working = true;
				}

				PluginExecutionResult result = binding.getSecond().invoke(context, resource.getInstance());

				String message = "";
				JComponent content = null;
				try {
					context.log("Starting visualization of " + resource);
					result.synchronize();
					content = result.getResult(binding.getFirst());
					if (content == null) {
						throw new Exception(resource.toString());
					}
				} catch (Exception e) {
					//throw new IllegalArgumentException("Failed to create visualization of " + resource, e);
					manager.getContext().getController().getMainView().showWorkspaceView();
					message = e.getMessage();
				} finally {
					context.getParentContext().deleteChild(context);

					component.removeAll();
					if (content != null) {
						try {
							content.repaint();
							component.add(content, BorderLayout.CENTER);
						} catch (Exception e) {
							e.printStackTrace();
							//ignore
							message = e.getMessage();
						}
					}
					if (component.getComponents().length == 0) {
						component.add(new JLabel("<HTML>Unable to produce visualization. Reason:<BR>"+message+"</HTML>"), BorderLayout.CENTER);
					}
					dialog.changeProgress(dialog.getMaximum());
					synchronized (ProMView.this) {
						working = false;
						ProMView.this.notifyAll();
					}
					manager.getContext().getController().getMainView().hideOverlay();
				}

			}
		});
		thread.start();

	}

	public synchronized boolean isReady() {
		return !working;
	}

}
