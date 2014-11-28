package org.but4reuse.adapters.graphs;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.but4reuse.adapters.IAdapter;
import org.but4reuse.adapters.IElement;
import org.but4reuse.utils.files.FileUtils;
import org.eclipse.core.runtime.IProgressMonitor;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.tg.TinkerGraph;
import com.tinkerpop.blueprints.util.io.graphml.GraphMLReader;
import com.tinkerpop.blueprints.util.io.graphml.GraphMLWriter;

/**
 * Graphs Adapter
 * 
 * @author jabier.martinez
 */
public class GraphsAdapter implements IAdapter {

	/**
	 * is a graphml file?
	 */
	@Override
	public boolean isAdaptable(URI uri, IProgressMonitor monitor) {
		File file = FileUtils.getFile(uri);
		if (file != null && file.exists() && !file.isDirectory() && FileUtils.isExtension(file, "graphml")) {
			return true;
		}
		return false;
	}

	/**
	 * Read the graph file
	 */
	@Override
	public List<IElement> adapt(URI uri, IProgressMonitor monitor) {
		List<IElement> elements = new ArrayList<IElement>();
		File file = FileUtils.getFile(uri);
		Graph graph = new TinkerGraph();
		GraphMLReader reader = new GraphMLReader(graph);

		InputStream is;
		try {
			is = new BufferedInputStream(new FileInputStream(file));
			reader.inputGraph(is);
		} catch (IOException e) {
			e.printStackTrace();
		}

		HashMap<Vertex, VertexElement> map = new HashMap<Vertex, VertexElement>();
		Iterable<Vertex> vertices = graph.getVertices();
		for (Vertex vertex : vertices) {
			VertexElement v = new VertexElement();
			v.setVertex(vertex);
			elements.add(v);
			map.put(vertex, v);
		}

		Iterable<Edge> edges = graph.getEdges();
		for (Edge edge : edges) {
			EdgeElement e = new EdgeElement();
			e.setEdge(edge);
			elements.add(e);
			// Add dependencies
			Vertex from = edge.getVertex(Direction.OUT);
			e.addDependency("from", map.get(from));
			Vertex to = edge.getVertex(Direction.IN);
			e.addDependency("to", map.get(to));
		}

		return elements;
	}

	@Override
	public void construct(URI uri, List<IElement> elements, IProgressMonitor monitor) {

		// Create graph
		Graph graph = new TinkerGraph();

		// Add vertices
		for (IElement element : elements) {
			if (element instanceof VertexElement) {
				VertexElement ve = (VertexElement) element;
				Vertex v = graph.addVertex(ve.getVertex().getId());
				// adding all its properties
				for(String key : ve.getVertex().getPropertyKeys()){
					v.setProperty(key, ve.getVertex().getProperty(key));
				}
			}
		}
		
		// Add edges
		// We create link vertices when they do not exist in the graph but the edge reference to it
		int linkVertex = 0;
		for (IElement element : elements) {
			if (element instanceof EdgeElement) {
				EdgeElement ee = (EdgeElement) element;
				Vertex v1 = ee.getEdge().getVertex(Direction.IN);
				Vertex v2 = ee.getEdge().getVertex(Direction.OUT);
				// If they dont exist, create fake ones
				if(graph.getVertex(v1.getId())==null){
					v1 = graph.addVertex("link vertex "+ linkVertex);
					v1.setProperty("label", v1.getId());
					linkVertex++;
				} else {
					// get the current object in the graph
					v1 = graph.getVertex(v1.getId());
				}
				if(graph.getVertex(v2.getId())==null){
					v2 = graph.addVertex("link vertex "+ linkVertex);
					v2.setProperty("label", v2.getId());
					linkVertex++;
				} else {
					// get the current object in the graph
					v2 = graph.getVertex(v2.getId());
				}
				
				Edge edge = graph.addEdge(ee.getEdge().getId(), v1, v2, ee.getEdge().getLabel());
				// adding all its properties
				for(String key : ee.getEdge().getPropertyKeys()){
					edge.setProperty(key, ee.getEdge().getProperty(key));
				}
			}
		}

		// Save
		GraphMLWriter writer = new GraphMLWriter(graph);
		writer.setNormalize(true);

		try {
			// Use the given file or use a default name if a folder was given
			if (uri.toString().endsWith("/")) {
				uri = new URI(uri.toString() + "graph.graphml");
			}
			// Create file if it does not exist
			File file = FileUtils.getFile(uri);
			FileUtils.createFile(file);

			writer.outputGraph(file.getAbsolutePath());
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
		}
	}

}
