
/*
 * Enhanced VNC Thumbnail Viewer 1.001
 *      - Modified for loop of pagination
 *      - Added calInitNext(), calInitPrevious(), findViewers() methods
 *      - Modified return value in next(), previous() methods
 * 
 * Enhanced VNC Thumbnail Viewer 1.000
 *      - Class for managing about page
 * 
 */

import java.util.ArrayList;
import java.util.List;

class Pagination {
	static int thumbsnailPerPage = 4;

	int page;
	VncViewersList viewersList;
	List<VncViewer> viewers;

	Pagination(VncViewersList v) {
		viewers = new ArrayList<VncViewer>();
		viewersList = v;
		page = 0;
	}

	public List<VncViewer> next() {
		setPage(page + 1);
		return viewers;
	}

	public List<VncViewer> previous() {
		setPage(page - 1);
		return viewers;
	}

	public boolean hasNext() {
		return isLimited();
	}

	public boolean hasPrevious() {
		return isLimited();
	}

	public boolean isLimited() {
		if (viewersList.size() > thumbsnailPerPage) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isEmpty() {
		if (viewersList.size() == 0) {
			return true;
		} else {
			return false;
		}
	}

	/*
	 * Added on evnctv 1.001
	 * Find viewers that will be shown
	 */
	private void setPage(int page) {
    	if (page < 0)
    		page = viewersList.size() / thumbsnailPerPage;
    	if (page * thumbsnailPerPage >= viewersList.size())
    		page = 0;
    	this.page = page;
    	int from = page *thumbsnailPerPage;
    	int to = from + thumbsnailPerPage;
    	if (to > viewersList.size())
    		to = viewersList.size();
        viewers = viewersList.subList(from, to);
    }
}
