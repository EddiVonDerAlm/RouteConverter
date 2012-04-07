/*
    This file is part of RouteConverter.

    RouteConverter is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    RouteConverter is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with RouteConverter; if not, write to the Free Software
    Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

    Copyright (C) 2007 Christian Pesch. All Rights Reserved.
*/
package slash.navigation.earthtools;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class EarthToolsServiceIT {
    private EarthToolsService service = new EarthToolsService();

    @Test
    public void testElevationFor() throws IOException {
        assertEquals(38, service.getElevationFor(11.2, 59.0).intValue());
        assertNull(service.getElevationFor(11.2, 60.0));
        assertNull(service.getElevationFor(11.2, 61.0));

        assertNull(service.getElevationFor(-68.0, -54.0));
        assertNull(service.getElevationFor(-68.0, -55.0));
        assertEquals(null, service.getElevationFor(-68.0, -56.0));
        assertEquals(null, service.getElevationFor(-68.0, -56.1));
        assertEquals(null, service.getElevationFor(-68.0, -57.0));
    }
}