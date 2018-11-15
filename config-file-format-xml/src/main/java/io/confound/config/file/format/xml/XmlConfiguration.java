/*
 * Copyright © 2018 GlobalMentor, Inc. <http://www.globalmentor.com/>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.confound.config.file.format.xml;

import static java.util.Objects.*;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.*;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import io.confound.config.AbstractStringConfiguration;
import io.confound.config.Configuration;
import io.confound.config.ConfigurationException;

/**
 * Implementation of a configuration based on a XML file format.
 * 
 * @author Magno N A Cruz
 */
public class XmlConfiguration extends AbstractStringConfiguration {

	private static final String HIERARCHY_DELIMITER = ".";

	private final Document xmlDocument;

	/**
	 * Constructor of the configuration.
	 * 
	 * @param xmlDocument The XML tree node to be used as the configuration.
	 */
	public XmlConfiguration(@Nonnull final Document xmlDocument) {
		this(xmlDocument, null);
	}

	/**
	 * Constructor of the configuration providing a fallback parent configuration.
	 * 
	 * @param xmlDocument The XML tree node to be used as the configuration.
	 * @param parentConfiguration The parent configuration.
	 */
	public XmlConfiguration(@Nonnull final Document xmlDocument, @Nullable final Configuration parentConfiguration) {
		super(parentConfiguration);
		this.xmlDocument = requireNonNull(xmlDocument);
	}

	@Override
	protected Optional<String> findParameterImpl(@Nonnull final String key) throws ConfigurationException {
		requireNonNull(key, "The key for the object to be looked up cannot be null.");

		Node parentNode = xmlDocument.getDocumentElement();

		String keySegment = key;

		// TODO improve key splitting algorithm.

		while(keySegment.contains(HIERARCHY_DELIMITER)) {
			final String currentKey = keySegment.substring(0, keySegment.indexOf(HIERARCHY_DELIMITER));

			parentNode = getChildNode(parentNode, currentKey);

			keySegment = keySegment.substring(keySegment.indexOf(HIERARCHY_DELIMITER) + 1);
		}

		return Optional.ofNullable(getChildNode(parentNode, keySegment)).map(Node::getTextContent);
	}

	/**
	 * Retrieves the child node with a given key in a parent node.
	 * 
	 * @param parentNode The parent node to be traversed.
	 * @param key The key of the node to be retrieved.
	 * @return The child node with the given key, or <code>null</code> if it doesn't exist.
	 */
	private Node getChildNode(@Nonnull final Node parentNode, @Nonnull final String key) {
		Objects.requireNonNull(key, "The key of the node to be retrieved cannot be null.");

		// TODO change approach to traverse nodes to something more efficient.

		Node childNode = null;

		final NodeList childNodes = parentNode.getChildNodes();

		for(int i = 0; i < childNodes.getLength(); i++) {
			final Node node = childNodes.item(i);

			if(node.getNodeType() == Node.ELEMENT_NODE) {
				final Element element = (Element)node;

				if(element.getNodeName().equals(key)) {
					childNode = element;
					break;
				}
			}

		}

		return childNode;
	}

}
