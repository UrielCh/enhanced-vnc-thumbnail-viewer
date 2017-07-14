//
//  Copyright (C) 2007-2008 David Czechowski  All Rights Reserved.
//
//  This is free software; you can redistribute it and/or modify
//  it under the terms of the GNU General Public License as published by
//  the Free Software Foundation; either version 2 of the License, or
//  (at your option) any later version.
//
//  This software is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//  GNU General Public License for more details.
//
//  You should have received a copy of the GNU General Public License
//  along with this software; if not, write to the Free Software
//  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,
//  USA.
//

/* *
 * Enhanced VNC Thumbnail Viewer 1.003
 *  - Added method about screen capture feature
 *
 * Enhanced VNC Thumbnail Viewer 1.001
 *  - Called proxy setting from Setting class
 * 
 * Enhanced VNC Thumbnail Viewer 1.0
 *  - New methods -> launchViewer()
 */

import java.awt.*;
import java.util.*;

//
// This Vector-based List is used to maintain of a list of VncViewers
//
// This also contains the ability to load/save it's list of VncViewers to/from
//    an external xml file.
//

@SuppressWarnings("serial")
public class VncViewersList extends ArrayList<VncViewer> {

	private EnhancedVncThumbnailViewer tnViewer;

	//
	// Constructor.
	//
	public VncViewersList(EnhancedVncThumbnailViewer v) {
		super();
		tnViewer = v;
	}

	//
	// If a host is loaded in first time, this method will be called
	//
	public VncViewer launchViewer(String host, int port, String password, String user, String userdomain, String compname) {
		VncViewer v = launchViewer(tnViewer, host, port, password, user, userdomain, compname);
		add(v);
		tnViewer.addViewer(v);
		return v;
	}

	//
	// Added on evnctv 1.000
	//    When want to reconnect, this will be called
	//
	public VncViewer launchViewerReconnect(String host, int port, String password, String user, String userdomain, String compname) {
		VncViewer v = launchViewer(tnViewer, host, port, password, user, userdomain, compname);
		tnViewer.addViewer(v);
		return v;
	}

	public VncViewer launchViewerReconnect(String host, int port, String password, String user, String userdomain, String compname, int order) {
		VncViewer v = launchViewer(tnViewer, host, port, password, user, userdomain, compname);
		tnViewer.addViewer(v, order);
		return v;
	}

	/* Added on evnctv 1.003 */
	public VncViewer launchViewerScreenCapture(String host, int port, String password, String user, String userdomain, String compname) {
		VncViewer v = launchViewer(tnViewer, host, port, password, user, userdomain, compname);
		tnViewer.addViewerScreenCapture(v);
		return v;
	}

	public static VncViewer launchViewer(EnhancedVncThumbnailViewer tnviewer, String host, int port, String password, String user, String userdomain,
			String compname) {
		ArrayList<String> args = new ArrayList<String>();
		args.add("host");
		args.add(host);
		args.add("port");
		args.add(Integer.toString(port));

		if (password != null && password.length() != 0) {
			args.add("password");
			args.add(password);
		}

		if (user != null && user.length() != 0) {
			args.add("username");
			args.add(user);
		}

		if (userdomain != null && userdomain.length() != 0) {
			args.add("userdomain");
			args.add(userdomain);
		}

		if (compname != null && compname.length() != 0) {
			args.add("compname");
			args.add(compname);
		}

		// launch a new viewer
		System.out.println("Launch Host: " + host + ":" + port);
		VncViewer v = new VncViewer();
		v.mainArgs = args.toArray(new String[args.size()]);
		v.inAnApplet = false;
		v.inSeparateFrame = false;
		v.showControls = true;
		v.showOfflineDesktop = true;
		v.vncFrame = tnviewer;
		v.init();
		v.options.viewOnly = true;
		v.options.autoScale = true; // false, because ThumbnailViewer maintains the scaling
		v.options.scalingFactor = 10;
		v.addContainerListener(tnviewer);
		v.start();

		return v;
	}

	public VncViewer getViewer(String hostname, int port) {
		for (VncViewer v : this)
			if (v.host.equals(hostname) && v.port == port)
				return v;
		return null;
	}

	public VncViewer getViewer(Container c) {
		for (VncViewer v : this)
			if (c.isAncestorOf(v))
				return v;
		return null;
	}

	public VncViewer getViewer(Button b) {
		for (VncViewer v : this)
			if (v.getParent().isAncestorOf(b))
				return v;
		return null;
	}
}