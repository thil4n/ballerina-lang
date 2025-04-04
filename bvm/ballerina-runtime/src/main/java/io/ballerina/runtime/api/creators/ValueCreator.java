/*
 *  Copyright (c) 2019, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  WSO2 Inc. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */

package io.ballerina.runtime.api.creators;

import io.ballerina.runtime.api.Module;
import io.ballerina.runtime.api.types.ArrayType;
import io.ballerina.runtime.api.types.FunctionType;
import io.ballerina.runtime.api.types.MapType;
import io.ballerina.runtime.api.types.RecordType;
import io.ballerina.runtime.api.types.StreamType;
import io.ballerina.runtime.api.types.TableType;
import io.ballerina.runtime.api.types.TupleType;
import io.ballerina.runtime.api.types.Type;
import io.ballerina.runtime.api.types.XmlNodeType;
import io.ballerina.runtime.api.values.BArray;
import io.ballerina.runtime.api.values.BDecimal;
import io.ballerina.runtime.api.values.BError;
import io.ballerina.runtime.api.values.BFunctionPointer;
import io.ballerina.runtime.api.values.BHandle;
import io.ballerina.runtime.api.values.BListInitialValueEntry;
import io.ballerina.runtime.api.values.BMap;
import io.ballerina.runtime.api.values.BMapInitialValueEntry;
import io.ballerina.runtime.api.values.BObject;
import io.ballerina.runtime.api.values.BStream;
import io.ballerina.runtime.api.values.BStreamingJson;
import io.ballerina.runtime.api.values.BString;
import io.ballerina.runtime.api.values.BTable;
import io.ballerina.runtime.api.values.BTypedesc;
import io.ballerina.runtime.api.values.BXml;
import io.ballerina.runtime.api.values.BXmlItem;
import io.ballerina.runtime.api.values.BXmlQName;
import io.ballerina.runtime.api.values.BXmlSequence;
import io.ballerina.runtime.internal.json.JsonDataSource;
import io.ballerina.runtime.internal.utils.RuntimeUtils;
import io.ballerina.runtime.internal.utils.ValueUtils;
import io.ballerina.runtime.internal.values.ArrayValue;
import io.ballerina.runtime.internal.values.ArrayValueImpl;
import io.ballerina.runtime.internal.values.DecimalValue;
import io.ballerina.runtime.internal.values.DecimalValueKind;
import io.ballerina.runtime.internal.values.FPValue;
import io.ballerina.runtime.internal.values.HandleValue;
import io.ballerina.runtime.internal.values.ListInitialValueEntry;
import io.ballerina.runtime.internal.values.MapValueImpl;
import io.ballerina.runtime.internal.values.MappingInitialValueEntry;
import io.ballerina.runtime.internal.values.StreamValue;
import io.ballerina.runtime.internal.values.StreamingJsonValue;
import io.ballerina.runtime.internal.values.TableValueImpl;
import io.ballerina.runtime.internal.values.TupleValueImpl;
import io.ballerina.runtime.internal.values.TypedescValueImpl;
import io.ballerina.runtime.internal.values.XmlItem;
import io.ballerina.runtime.internal.values.XmlQName;
import io.ballerina.runtime.internal.values.XmlSequence;
import io.ballerina.runtime.internal.xml.XmlFactory;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import javax.xml.namespace.QName;

/**
 * Class @{@link ValueCreator} provides apis to create ballerina value instances.
 *
 * @since 1.1.0
 */
public final class ValueCreator {

    /**
     * Creates a new array with given array type.
     *
     * @param type the {@code ArrayType} object representing the type
     * @return     the new array
     */
    public static BArray createArrayValue(ArrayType type) {
        return new ArrayValueImpl(type);
    }

    /**
     * Creates a new integer array.
     *
     * @param values initial array values
     * @return       integer array
     */
    public static BArray createArrayValue(long[] values) {
        return new ArrayValueImpl(values, false);
    }

    /**
     * Creates a new integer array.
     *
     * @param values initial array values
     * @return       integer array
     */
    public static BArray createReadonlyArrayValue(long[] values) {
        return new ArrayValueImpl(values, true);
    }

    /**
     * Creates a new boolean array.
     *
     * @param values initial array values
     * @return       boolean array
     */
    public static BArray createArrayValue(boolean[] values) {
        return new ArrayValueImpl(values, false);
    }

    /**
     * Creates a new boolean array.
     *
     * @param values initial array values
     * @return       boolean array
     */
    public static BArray createReadonlyArrayValue(boolean[] values) {
        return new ArrayValueImpl(values, true);
    }

    /**
     * Creates a new byte array.
     *
     * @param values initial array values
     * @return       byte array
     */
    public static BArray createArrayValue(byte[] values) {
        return new ArrayValueImpl(values, false);
    }

    /**
     * Creates a new byte array.
     *
     * @param values initial array values
     * @return       byte array
     */
    public static BArray createReadonlyArrayValue(byte[] values) {
        return new ArrayValueImpl(values, true);
    }

    /**
     * Creates a new float array.
     *
     * @param values initial array values
     * @return       float array
     */
    public static BArray createArrayValue(double[] values) {
        return new ArrayValueImpl(values, false);
    }

    /**
     * Creates a new float array.
     *
     * @param values initial array values
     * @return       float array
     */
    public static BArray createReadonlyArrayValue(double[] values) {
        return new ArrayValueImpl(values, true);
    }

    /**
     * Creates a new string array.
     *
     * @param values initial array values
     * @return       string array
     */
    public static BArray createArrayValue(BString[] values) {
        return new ArrayValueImpl(values, false);
    }

    /**
     * Creates a new string array.
     *
     * @param values initial array values
     * @return       string array
     */
    public static BArray createReadonlyArrayValue(BString[] values) {
        return new ArrayValueImpl(values, true);
    }

    /**
     * Create a new Ref value array.
     *
     * @param values initial array values
     * @param type   {@code ArrayType} of the array.
     * @return       ref Value array
     */
    public static BArray createArrayValue(Object[] values, ArrayType type) {
        return new ArrayValueImpl(values, type);
    }

    /**
     * Create a ref value array with given maximum length.
     *
     * @param type   {@code ArrayType} of the array.
     * @param length maximum length
     * @return       fixed length ref value array
     * @deprecated   use {@link #createArrayValue(ArrayType)} instead
     */
    @Deprecated
    public static BArray createArrayValue(ArrayType type, int length) {
        return new ArrayValueImpl(type, length);
    }

    /**
     * Create a ref value array with given maximum length.
     *
     * @param type          {@code ArrayType} of the array.
     * @param size          array size
     * @param initialValues initial values
     * @return              fixed length ref value array
     * @deprecated          use {@link #createArrayValue(ArrayType, BListInitialValueEntry[])} instead
     */
    @Deprecated
    public static BArray createArrayValue(ArrayType type, long size, BListInitialValueEntry[] initialValues) {
        return new ArrayValueImpl(type, size, initialValues);
    }

    /**
     *
     * Create a ref value array with given type and initial values.
     *
     * @param type          {@code ArrayType} of the array.
     * @param initialValues initial values
     * @return              ref value array
     */
    public static BArray createArrayValue(ArrayType type, BListInitialValueEntry[] initialValues) {
        return new ArrayValueImpl(type, initialValues);
    }

    /**
     * Creates a new tuple with given tuple type.
     *
     * @param type the {@code TupleType} object representing the type
     * @return     the new array
     */
    public static BArray createTupleValue(TupleType type) {
        return new TupleValueImpl(type);
    }

    /**
     * Creates a new tuple with given tuple type.
     *
     * @param type          the {@code TupleType} object representing the type
     * @param size          size of the tuple
     * @param initialValues initial values
     * @return              the new tuple
     * @deprecated use {@link #createTupleValue(TupleType, BListInitialValueEntry[])} instead
     */
    @Deprecated
    public static BArray createTupleValue(TupleType type, long size, BListInitialValueEntry[] initialValues) {
        return new TupleValueImpl(type, size, initialValues);
    }

    /**
     * Creates a new tuple with given tuple type.
     *
     * @param type          the {@code TupleType} object representing the type
     * @param initialValues initial values
     * @return              the new tuple
     */
    public static BArray createTupleValue(TupleType type, BListInitialValueEntry[] initialValues) {
        return new TupleValueImpl(type, initialValues);
    }

    /**
     * Create a decimal from given value.
     *
     * @param value the value of the decimal
     * @return      decimal value
     */
    public static BDecimal createDecimalValue(BigDecimal value) {
        return new DecimalValue(value);
    }

    /**
     * Create a decimal from given string.
     *
     * @param value string value
     * @return      decimal value
     */
    public static BDecimal createDecimalValue(String value) {
        return new DecimalValue(value);
    }

    // TODO: remove this with https://github.com/ballerina-platform/ballerina-lang/issues/40175
    /**
     * Create a decimal from given string and value kind.
     *
     * @param value     string value
     * @param valueKind value kind
     * @return          decimal value
     * @deprecated      use {@link #createDecimalValue(String)} instead.
     */
    @Deprecated(since = "2201.6.0", forRemoval = true)
    public static BDecimal createDecimalValue(String value, DecimalValueKind valueKind) {
        return new DecimalValue(value, valueKind);
    }

    /**
     * Create function pointer to the given function with given {@code FunctionType}.
     *
     * @param function pointing function
     * @param type     {@code FunctionType} of the function pointer
     * @return         function pointer
     */
    public static BFunctionPointer createFPValue(Function function, FunctionType type) {
        return new FPValue(function, type, null, false);
    }

    /**
     * Create function pointer to the given function with given {@code FunctionType}.
     *
     * @param function   pointing function
     * @param type       {@code FunctionType} of the function pointer
     * @param strandName name for newly creating strand which is used to run the function pointer
     * @return           function pointer
     */
    public static BFunctionPointer createFPValue(Function function, FunctionType type, String strandName) {
        return new FPValue(function, type, strandName, false);
    }

    // TODO: remove this with https://github.com/ballerina-platform/ballerina-lang/issues/40175
    /**
     * Create {@code StreamingJsonValue} with given datasource.
     *
     * @param datasource {@code JSONDataSource} to be used
     * @return           created {@code StreamingJsonValue}
     * @deprecated
     */
    @Deprecated(since = "2201.6.0", forRemoval = true)
    public static BStreamingJson createStreamingJsonValue(JsonDataSource datasource) {
        return new StreamingJsonValue(datasource);
    }

    /**
     * Create a stream with given constraint type.
     *
     * @param type constraint type
     * @return     stream value
     */
    public static BStream createStreamValue(StreamType type) {
        return new StreamValue(type);
    }

    /**
     * Create a stream with given constraint type and iterator object.
     *
     * @param type        constraint type
     * @param iteratorObj iterator object
     * @return            stream value
     */
    public static BStream createStreamValue(StreamType type, BObject iteratorObj) {
        return new StreamValue(type, iteratorObj);
    }

    /**
     * Create a type descriptor value.
     *
     * @param describingType {@code Type} of the value describe by this value
     * @return               type descriptor
     */
    public static BTypedesc createTypedescValue(Type describingType) {
        return new TypedescValueImpl(describingType);
    }

    /**
     * Create an empty {@code BXmlItem}.
     *
     * @return {@code BXmlItem}
     */
    public static BXmlItem createXmlItem() {
        return new XmlItem(new QName(null), new XmlSequence());
    }

    /**
     * Create a {@code BXmlItem} from a XML string.
     *
     * @param name     QName
     * @param children Xml sequence
     * @return         {@code BXmlItem}
     */
    public static BXmlItem createXmlItem(QName name, BXmlSequence children) {
        return new XmlItem(name, (XmlSequence) children);
    }

    /**
     * Create a {@code BXmlItem} from a XML string.
     *
     * @param name QName
     * @return     {@code BXmlItem}
     */
    public static BXmlItem createXmlItem(QName name) {
        return new XmlItem(name);
    }

    /**
     * Create a {@code BXmlItem} from a XML string.
     *
     * @param name     QName
     * @param readonly Whether the element is immutable
     * @return         {@code BXmlItem}
     */
    public static BXmlItem createXmlItem(QName name, boolean readonly) {
        return new XmlItem(name, readonly);
    }

    /**
     * Create a {@code BXmlItem} from a XML string.
     *
     * @param name     QName
     * @param children Xml sequence
     * @param readonly Whether the element is immutable
     * @return         {@code BXmlItem}
     */
    public static BXmlItem createXmlItem(QName name, BXmlSequence children, boolean readonly) {
        return new XmlItem(name, (XmlSequence) children, readonly);
    }

    /**
     * Create a {@code BXml} from a XML string.
     *
     * @param xmlValue A XML string
     * @return         {@code BXml}
     */
    public static BXml createXmlValue(String xmlValue) {
        return XmlFactory.parse(xmlValue);
    }

    /**
     * Create a {@code BXml} from a {@link InputStream}.
     *
     * @param inputStream Input Stream
     * @return            {@code BXml}
     */
    public static BXml createXmlValue(InputStream inputStream) {
        return XmlFactory.parse(inputStream);
    }

    /**
     * Create an element type BXml.
     *
     * @param startTagName Name of the start tag of the element
     * @param endTagName   Name of the end tag of element
     * @param defaultNsUri Default namespace URI
     * @return             BXml Element type BXml
     */
    @Deprecated
    public static BXml createXmlValue(BXmlQName startTagName, BXmlQName endTagName, String defaultNsUri) {
        return XmlFactory.createXMLElement(startTagName, endTagName, defaultNsUri);
    }

    /**
     * Create an element type BXml.
     *
     * @param startTagName Name of the start tag of the element
     * @param defaultNsUri Default namespace URI
     * @return             BXml Element type BXml
     */
    @Deprecated
    public static BXml createXmlValue(BXmlQName startTagName, String defaultNsUri) {
        return XmlFactory.createXMLElement(startTagName, defaultNsUri);
    }

    public static BXml createXmlValue(BXmlQName startTagName, BString defaultNsUriVal) {
        return XmlFactory.createXMLElement(startTagName, defaultNsUriVal);
    }

    /**
     * Create an element type BXml, specifying the type which will indicate mutability.
     *
     * @param startTagName Name of the start tag of the element
     * @param defaultNsUri Default namespace URI
     * @param readonly     Whether the element is immutable
     * @return             BXml Element type BXml
     */
    @Deprecated
    public static BXml createXmlValue(BXmlQName startTagName, String defaultNsUri, boolean readonly) {
        return XmlFactory.createXMLElement(startTagName, defaultNsUri, readonly);
    }

    public static BXml createXmlValue(BXmlQName startTagName, BString defaultNsUriVal, boolean readonly) {
        return XmlFactory.createXMLElement(startTagName, defaultNsUriVal, readonly);
    }

    /**
     * Create XML qualified name with qualified name string and namespace prefix.
     *
     * @param qNameStr Qualified name string
     * @param prefix   Namespace prefix
     * @return         XML qualified name
     */
    public static BXmlQName createXmlQName(BString qNameStr, String prefix) {
        return new XmlQName(qNameStr.getValue(), prefix);
    }

    /**
     * Create XML qualified name with local name, uri and namespace prefix.
     *
     * @param localName Local part of the qualified name
     * @param uri       Namespace URI
     * @param prefix    Namespace prefix
     * @return          XML qualified name
     */
    public static BXmlQName createXmlQName(String localName, String uri, String prefix) {
        return new XmlQName(localName, uri, prefix);
    }

    /**
     * Create XML qualified name with local name, uri and namespace prefix.
     *
     * @param localName Local part of the qualified name
     * @param uri       Namespace URI
     * @param prefix    Namespace prefix
     * @return          XML qualified name
     */
    public static BXmlQName createXmlQName(BString localName, BString uri, BString prefix) {
        return new XmlQName(localName, uri, prefix);
    }

    /**
     * Create XML qualified name with a qualified name string.
     *
     * @param qNameStr qualified name string
     * @return         XML qualified name
     */
    public static BXmlQName createXmlQName(String qNameStr) {
        return new XmlQName(qNameStr);
    }

    /**
     * Create XML qualified name with a qualified name string.
     *
     * @param qNameStr qualified name string
     * @return         XML qualified name
     */
    public static BXmlQName createXmlQName(BString qNameStr) {
        return new XmlQName(qNameStr);
    }

    /**
     * Create an empty xml sequence.
     *
     * @return xml sequence
     */
    public static BXmlSequence createXmlSequence() {
        return XmlFactory.createXmlSequence();
    }

    /**
     * Create a {@code XMLSequence} from a {@link org.apache.axiom.om.OMNode} object.
     *
     * @param sequence xml sequence array
     * @return         xml sequence
     */
    public static BXmlSequence createXmlSequence(BArray sequence) {
        return XmlFactory.createXmlSequence(sequence);
    }

    /**
     * Create a {@code XMLSequence} from a {@link org.apache.axiom.om.OMNode} object.
     *
     * @param sequence xml sequence array
     * @return         xml sequence
     */
    public static BXmlSequence createXmlSequence(List<BXml> sequence) {
        List<BXml> flattenedSequence = new ArrayList<>();
        for (BXml item : sequence) {
            if (item.getNodeType() == XmlNodeType.SEQUENCE) {
                flattenedSequence.addAll(((XmlSequence) item).getChildrenList());
            } else {
                flattenedSequence.add(item);
            }
        }
        return XmlFactory.createXmlSequence(flattenedSequence);
    }

    /**
     * Create a {@code XMLSequence} from a {@link org.apache.axiom.om.OMNode} object.
     *
     * @param child xml content
     * @return      xml sequence
     */
    public static BXmlSequence createXmlSequence(BXml child) {
        return XmlFactory.createXmlSequence(child);
    }

    /**
     * Create a comment type BXml.
     *
     * @param content Comment content
     * @return        Comment type BXml
     */
    @Deprecated
    public static BXml createXmlComment(String content) {
        return XmlFactory.createXMLComment(content);
    }

    /**
     * Create a comment type BXml.
     *
     * @param content Comment content
     * @return        Comment type BXml
     */
    public static BXml createXmlComment(BString content) {
        return XmlFactory.createXMLComment(content.getValue());
    }

    /**
     * Create a comment type BXml, specifying the type which will indicate mutability.
     *
     * @param content  Comment content
     * @param readonly Whether the comment is immutable
     * @return         Comment type BXml
     */
    @Deprecated
    public static BXml createXmlComment(String content, boolean readonly) {
        return XmlFactory.createXMLComment(content, readonly);
    }

    /**
     * Create a comment type BXml, specifying the type which will indicate mutability.
     *
     * @param content  Comment content
     * @param readonly Whether the comment is immutable
     * @return         Comment type BXml
     */
    public static BXml createXmlComment(BString content, boolean readonly) {
        return XmlFactory.createXMLComment(content, readonly);
    }

    /**
     * Create a comment type BXml.
     *
     * @param content Text content
     * @return        Text type BXml
     */
    @Deprecated
    public static BXml createXmlText(String content) {
        return XmlFactory.createXMLText(content);
    }

    /**
     * Create a comment type BXml.
     *
     * @param content Text content
     * @return        Text type BXml
     */
    public static BXml createXmlText(BString content) {
        return XmlFactory.createXMLText(content);
    }

    /**
     * Create a processing instruction type BXml.
     *
     * @param target PI target
     * @param data   PI data
     * @return       Processing instruction type BXml
     */
    @Deprecated
    public static BXml createXmlProcessingInstruction(String target, String data) {
        return XmlFactory.createXMLProcessingInstruction(data, target);
    }

    /**
     * Create a processing instruction type BXml.
     *
     * @param target PI target
     * @param data   PI data
     * @return       Processing instruction type BXml
     */
    public static BXml createXmlProcessingInstruction(BString target, BString data) {
        return XmlFactory.createXMLProcessingInstruction(target, data);
    }

    /**
     * Create a processing instruction type BXml, specifying the type which will indicate mutability.
     *
     * @param target   PI target
     * @param data     PI data
     * @param readonly Whether the PI is immutable
     * @return         Processing instruction type BXml
     */
    @Deprecated
    public static BXml createXmlProcessingInstruction(String target, String data, boolean readonly) {
        return XmlFactory.createXMLProcessingInstruction(data, target, readonly);
    }

    /**
     * Create a processing instruction type BXml, specifying the type which will indicate mutability.
     *
     * @param target   PI target
     * @param data     PI data
     * @param readonly Whether the PI is immutable
     * @return         Processing instruction type BXml
     */
    public static BXml createXmlProcessingInstruction(BString target, BString data, boolean readonly) {
        return XmlFactory.createXMLProcessingInstruction(target, data, readonly);
    }

    /**
     * Create a runtime map value.
     *
     * @return value of the record.
     */
    public static BMap<BString, Object> createMapValue() {
        return new MapValueImpl<>();
    }

    /**
     * Create a runtime map value with given type.
     *
     * @param mapType map type
     * @return        map value
     * @deprecated    use {@link #createMapValue(MapType)} instead
     */
    @Deprecated
    public static BMap<BString, Object> createMapValue(Type mapType) {
        return new MapValueImpl<>(mapType);
    }

    /**
     * Create a runtime map value with given map type.
     *
     * @param mapType map type
     * @return        map value
     */
    public static BMap<BString, Object> createMapValue(MapType mapType) {
        return new MapValueImpl<>(mapType);
    }

    /**
     * Create a runtime map value with given initial values and given type.
     *
     * @param mapType   map type
     * @param keyValues initial map values to be populated
     * @return          map value
     * @deprecated      use {@link #createMapValue(MapType, BMapInitialValueEntry[])} instead
     */
    @Deprecated
    public static BMap<BString, Object> createMapValue(Type mapType, BMapInitialValueEntry[] keyValues) {
        return new MapValueImpl<>(mapType, keyValues);
    }

    /**
     * Create a runtime map value with given initial values and given type.
     *
     * @param recordType record type
     * @param keyValues  initial map values to be populated
     * @return map value
     */
    public static BMap<BString, Object> createMapValue(RecordType recordType, BMapInitialValueEntry[] keyValues) {
        return new MapValueImpl<>(recordType, keyValues);
    }

    /**
     * Create a runtime map value with given initial values and given map type.
     *
     * @param mapType   map type
     * @param keyValues initial map values to be populated
     * @return          map value
     */
    public static BMap<BString, Object> createMapValue(MapType mapType, BMapInitialValueEntry[] keyValues) {
        return new MapValueImpl<>(mapType, keyValues);
    }

    /**
     * Create a key field entry in a mapping constructor expression.
     *
     * @param key   key
     * @param value value
     * @return      key field entry
     */
    public static BMapInitialValueEntry createKeyFieldEntry(Object key, Object value) {
        return new MappingInitialValueEntry.KeyValueEntry(key, value);
    }

    /**
     * Create a spread field entry in a mapping constructor expression.
     *
     * @param mappingValue mapping value
     * @return             spread field entry
     */
    public static BMapInitialValueEntry createSpreadFieldEntry(BMap<?, ?> mappingValue) {
        return new MappingInitialValueEntry.SpreadFieldEntry(mappingValue);
    }

    /**
     * Create a list initial value entry.
     *
     * @param value value
     * @return      list initial value entry
     */
    public static BListInitialValueEntry createListInitialValueEntry(Object value) {
        return new ListInitialValueEntry.ExpressionEntry(value);
    }

    /**
     * Create a runtime record value with given record type.
     *
     * <p>This method is used to create a {@link BMap} from the given java
     * instance of record type which can only be used inside java native code.
     * If the recordType is defined in the Ballerina code and the created map
     * value needs to be used in Ballerina, please use createRecordValue APIs
     * with packageId and recordTypeName to create a {@link BMap}.
     *
     * @param recordType record type
     * @return           record value
     */
    public static BMap<BString, Object> createRecordValue(RecordType recordType) {
        return new MapValueImpl<>(recordType);
    }

    /**
     * Create a runtime record value with given initial values and given record type.
     *
     * <p>This method is used to create a {@link BMap} from the given java
     * instance of record type which can only be used inside java native code.
     * If the recordType is defined in the Ballerina code and the created map
     * value needs to be used in Ballerina, please use createRecordValue APIs
     * with packageId and recordTypeName to create a {@link BMap}.
     *
     * @param recordType record type
     * @param keyValues  initial map values to be populated
     * @return           record value
     */
    public static BMap<BString, Object> createRecordValue(RecordType recordType, BMapInitialValueEntry[] keyValues) {
        return new MapValueImpl<>(recordType, keyValues);
    }

    /**
     * Create a record value using the given package ID and record type name.
     *
     * @param packageId      the package ID where the record type is defined
     * @param recordTypeName name of the record type
     * @return               value of the record
     * @throws BError        if given record type is not defined in the ballerina module.
     */
    public static BMap<BString, Object> createRecordValue(Module packageId, String recordTypeName) throws BError {
        return ValueUtils.createRecordValue(packageId, recordTypeName);
    }

    /**
     * Create a record value that populates record fields using the given package ID, record type name and a map of
     * field names and associated values for the fields.
     *
     * @param packageId      the package ID where the record type is defined
     * @param recordTypeName name of the record type
     * @param valueMap       values to be used for fields when creating the record
     * @return               value of the populated record
     * @throws BError        if given record type is not defined in the ballerina module.
     */
    public static BMap<BString, Object> createRecordValue(Module packageId, String recordTypeName,
                                                          Map<String, Object> valueMap) throws BError {
        valueMap = RuntimeUtils.validateBMapValues(valueMap);
        return ValueUtils.createRecordValue(packageId, recordTypeName, valueMap);
    }

    /**
     * Create a readonly record value that populates record fields using the given package ID, record type name and a
     * map of field names and associated values for the fields.
     *
     * @param packageId      the package ID where the record type is defined
     * @param recordTypeName name of the record type
     * @param valueMap       values to be used for fields when creating the record
     * @return               value of the populated record
     * @throws BError        if given record type is not defined in the ballerina module.
     */
    public static BMap<BString, Object> createReadonlyRecordValue(Module packageId, String recordTypeName,
                                                                  Map<String, Object> valueMap) throws BError {
        valueMap = RuntimeUtils.validateBMapValues(valueMap);
        MapValueImpl<BString, Object> bMapValue = (MapValueImpl<BString, Object>) ValueUtils.createRecordValue(
                packageId, recordTypeName, valueMap);
        bMapValue.freezeDirect();
        return bMapValue;
    }

    /**
     * Create a record value that populates record fields using the given package ID, record type name and
     * a {@link BMap} of field names and associated values for the fields.
     *
     * @param packageId      the package ID where the record type is defined
     * @param recordTypeName name of the record type
     * @param valueMap       {@link BMap} of fields and values to initialize the record
     * @return               value of the populated record
     * @throws BError        if given record type is not defined in the ballerina module.
     */
    public static BMap<BString, Object> createRecordValue(Module packageId, String recordTypeName,
                                                          BMap<BString, Object> valueMap) throws BError {
        valueMap = RuntimeUtils.validateBMapValues(valueMap);
        return ValueUtils.createRecordValue(packageId, recordTypeName, valueMap);
    }

    /**
     * Create a readonly record value that populates record fields using the given package ID, record type name and
     * a {@link BMap} of field names and associated values for the fields.
     *
     * @param packageId      the package ID where the record type is defined
     * @param recordTypeName name of the record type
     * @param valueMap       {@link BMap} of fields and values to initialize the record
     * @return               value of the populated record
     * @throws BError        if given record type is not defined in the ballerina module.
     */
    public static BMap<BString, Object> createReadonlyRecordValue(Module packageId, String recordTypeName,
                                                                  BMap<BString, Object> valueMap) throws BError {
        valueMap = RuntimeUtils.validateBMapValues(valueMap);
        MapValueImpl<BString, Object> bMapValue = (MapValueImpl<BString, Object>) ValueUtils.createRecordValue(
                packageId, recordTypeName, valueMap);
        bMapValue.freezeDirect();
        return bMapValue;
    }

    /**
     * Populate a runtime record value with given field values.
     *
     * @param record which needs to get populated
     * @param values field values of the record
     * @return       value of the record
     */
    public static BMap<BString, Object> createRecordValue(BMap<BString, Object> record, Object... values) {
        record = RuntimeUtils.validateBMapValues(record);
        return ValueUtils.createRecordValue(record, values);
    }

    /**
     * Create an object value using the given package ID and object type name.
     *
     * @param packageId      the package ID that the object type resides
     * @param objectTypeName name of the object type
     * @param fieldValues    values to be used for fields when creating the object value instance
     * @return               value of the object
     * @throws BError        if given object type is not defined in the ballerina module.
     */
    public static BObject createObjectValue(Module packageId, String objectTypeName, Object... fieldValues)
            throws BError {
        return ValueUtils.createObjectValue(packageId, objectTypeName, fieldValues);
    }

    /**
     * Create an handle value using the given value.
     *
     * @param value object value
     * @return      handle value for given object value
     */
    public static BHandle createHandleValue(Object value) {
        return new HandleValue(value);
    }

    /**
     * Create an table value using the given type.
     *
     * @param tableType table type
     * @return          table value for given type
     */
    public static BTable<?, ?> createTableValue(TableType tableType) {
        return new TableValueImpl<>(tableType);
    }

    /**
     * Create an table value using the given type.
     *
     * @param tableType  table type
     * @param data       table data
     * @param fieldNames table field names
     * @return           table value for given type
     */
    public static BTable<?, ?> createTableValue(TableType tableType, BArray data, BArray fieldNames) {
        return new TableValueImpl<>(tableType, (ArrayValue) data, (ArrayValue) fieldNames);
    }

    private ValueCreator() {
    }
}
