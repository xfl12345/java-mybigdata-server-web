package cc.xfl12345.mybigdata.server.model.pojo;

import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.victools.jsonschema.generator.*;
import com.github.victools.jsonschema.generator.naming.SchemaDefinitionNamingStrategy;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class SchemaGeneratorConfigProxy implements SchemaGeneratorConfig {
    protected SchemaGeneratorConfig schemaGeneratorConfig;

    public SchemaGeneratorConfigProxy(SchemaGeneratorConfigBuilder builder) {
        schemaGeneratorConfig = builder.build();
    }

    public SchemaGeneratorConfig getSchemaGeneratorConfig() {
        return schemaGeneratorConfig;
    }

    public SchemaVersion getSchemaVersion() {
        return schemaGeneratorConfig.getSchemaVersion();
    }

    public String getKeyword(SchemaKeyword keyword) {
        return schemaGeneratorConfig.getKeyword(keyword);
    }

    public boolean shouldCreateDefinitionsForAllObjects() {
        return schemaGeneratorConfig.shouldCreateDefinitionsForAllObjects();
    }

    public boolean shouldCreateDefinitionForMainSchema() {
        return schemaGeneratorConfig.shouldCreateDefinitionForMainSchema();
    }

    public boolean shouldInlineAllSchemas() {
        return schemaGeneratorConfig.shouldInlineAllSchemas();
    }

    public boolean shouldIncludeSchemaVersionIndicator() {
        return schemaGeneratorConfig.shouldIncludeSchemaVersionIndicator();
    }

    public boolean shouldUsePlainDefinitionKeys() {
        return schemaGeneratorConfig.shouldUsePlainDefinitionKeys();
    }

    public boolean shouldIncludeExtraOpenApiFormatValues() {
        return schemaGeneratorConfig.shouldIncludeExtraOpenApiFormatValues();
    }

    public boolean shouldCleanupUnnecessaryAllOfElements() {
        return schemaGeneratorConfig.shouldCleanupUnnecessaryAllOfElements();
    }

    public boolean shouldIncludeStaticFields() {
        return schemaGeneratorConfig.shouldIncludeStaticFields();
    }

    public boolean shouldIncludeStaticMethods() {
        return schemaGeneratorConfig.shouldIncludeStaticMethods();
    }

    public boolean shouldDeriveFieldsFromArgumentFreeMethods() {
        return schemaGeneratorConfig.shouldDeriveFieldsFromArgumentFreeMethods();
    }

    public boolean shouldRepresentSingleAllowedValueAsConst() {
        return schemaGeneratorConfig.shouldRepresentSingleAllowedValueAsConst();
    }

    public boolean shouldAllowNullableArrayItems() {
        return schemaGeneratorConfig.shouldAllowNullableArrayItems();
    }

    public ObjectMapper getObjectMapper() {
        return schemaGeneratorConfig.getObjectMapper();
    }

    public ObjectNode createObjectNode() {
        return schemaGeneratorConfig.createObjectNode();
    }

    public ArrayNode createArrayNode() {
        return schemaGeneratorConfig.createArrayNode();
    }

    public int sortProperties(MemberScope<?, ?> first, MemberScope<?, ?> second) {
        return schemaGeneratorConfig.sortProperties(first, second);
    }

    public SchemaDefinitionNamingStrategy getDefinitionNamingStrategy() {
        return schemaGeneratorConfig.getDefinitionNamingStrategy();
    }

    public <M extends MemberScope<?, ?>> CustomDefinition getCustomDefinition(M scope, SchemaGenerationContext context, CustomPropertyDefinitionProvider<M> ignoredDefinitionProvider) {
        return schemaGeneratorConfig.getCustomDefinition(scope, context, ignoredDefinitionProvider);
    }

    public CustomDefinition getCustomDefinition(ResolvedType javaType, SchemaGenerationContext context, CustomDefinitionProviderV2 ignoredDefinitionProvider) {
        return schemaGeneratorConfig.getCustomDefinition(javaType, context, ignoredDefinitionProvider);
    }

    public List<ResolvedType> resolveSubtypes(ResolvedType javaType, SchemaGenerationContext context) {
        return schemaGeneratorConfig.resolveSubtypes(javaType, context);
    }

    public List<TypeAttributeOverrideV2> getTypeAttributeOverrides() {
        return schemaGeneratorConfig.getTypeAttributeOverrides();
    }

    public List<InstanceAttributeOverrideV2<FieldScope>> getFieldAttributeOverrides() {
        return schemaGeneratorConfig.getFieldAttributeOverrides();
    }

    public List<InstanceAttributeOverrideV2<MethodScope>> getMethodAttributeOverrides() {
        return schemaGeneratorConfig.getMethodAttributeOverrides();
    }

    public boolean isNullable(FieldScope field) {
        return schemaGeneratorConfig.isNullable(field);
    }

    public boolean isNullable(MethodScope method) {
        return schemaGeneratorConfig.isNullable(method);
    }

    public boolean isRequired(FieldScope field) {
        return schemaGeneratorConfig.isRequired(field);
    }

    public boolean isRequired(MethodScope method) {
        return schemaGeneratorConfig.isRequired(method);
    }

    public boolean isReadOnly(FieldScope field) {
        return schemaGeneratorConfig.isReadOnly(field);
    }

    public boolean isReadOnly(MethodScope method) {
        return schemaGeneratorConfig.isReadOnly(method);
    }

    public boolean isWriteOnly(FieldScope field) {
        return schemaGeneratorConfig.isWriteOnly(field);
    }

    public boolean isWriteOnly(MethodScope method) {
        return schemaGeneratorConfig.isWriteOnly(method);
    }

    public boolean shouldIgnore(FieldScope field) {
        return schemaGeneratorConfig.shouldIgnore(field);
    }

    public boolean shouldIgnore(MethodScope method) {
        return schemaGeneratorConfig.shouldIgnore(method);
    }

    @Deprecated
    public ResolvedType resolveTargetTypeOverride(FieldScope field) {
        return schemaGeneratorConfig.resolveTargetTypeOverride(field);
    }

    @Deprecated
    public ResolvedType resolveTargetTypeOverride(MethodScope method) {
        return schemaGeneratorConfig.resolveTargetTypeOverride(method);
    }

    public List<ResolvedType> resolveTargetTypeOverrides(FieldScope field) {
        return schemaGeneratorConfig.resolveTargetTypeOverrides(field);
    }

    public List<ResolvedType> resolveTargetTypeOverrides(MethodScope method) {
        return schemaGeneratorConfig.resolveTargetTypeOverrides(method);
    }

    public String resolvePropertyNameOverride(FieldScope field) {
        return schemaGeneratorConfig.resolvePropertyNameOverride(field);
    }

    public String resolvePropertyNameOverride(MethodScope method) {
        return schemaGeneratorConfig.resolvePropertyNameOverride(method);
    }

    public String resolveIdForType(TypeScope scope) {
        return schemaGeneratorConfig.resolveIdForType(scope);
    }

    public String resolveAnchorForType(TypeScope scope) {
        return schemaGeneratorConfig.resolveAnchorForType(scope);
    }

    public String resolveTitle(FieldScope field) {
        return schemaGeneratorConfig.resolveTitle(field);
    }

    public String resolveTitle(MethodScope method) {
        return schemaGeneratorConfig.resolveTitle(method);
    }

    public String resolveTitleForType(TypeScope scope) {
        return schemaGeneratorConfig.resolveTitleForType(scope);
    }

    public String resolveDescription(FieldScope field) {
        return schemaGeneratorConfig.resolveDescription(field);
    }

    public String resolveDescription(MethodScope method) {
        return schemaGeneratorConfig.resolveDescription(method);
    }

    public String resolveDescriptionForType(TypeScope scope) {
        return schemaGeneratorConfig.resolveDescriptionForType(scope);
    }

    public Object resolveDefault(FieldScope field) {
        return schemaGeneratorConfig.resolveDefault(field);
    }

    public Object resolveDefault(MethodScope method) {
        return schemaGeneratorConfig.resolveDefault(method);
    }

    public Object resolveDefaultForType(TypeScope scope) {
        return schemaGeneratorConfig.resolveDefaultForType(scope);
    }

    public Collection<?> resolveEnum(FieldScope field) {
        return schemaGeneratorConfig.resolveEnum(field);
    }

    public Collection<?> resolveEnum(MethodScope method) {
        return schemaGeneratorConfig.resolveEnum(method);
    }

    public Collection<?> resolveEnumForType(TypeScope scope) {
        return schemaGeneratorConfig.resolveEnumForType(scope);
    }

    public Type resolveAdditionalProperties(FieldScope field) {
        return schemaGeneratorConfig.resolveAdditionalProperties(field);
    }

    public Type resolveAdditionalProperties(MethodScope method) {
        return schemaGeneratorConfig.resolveAdditionalProperties(method);
    }

    public Type resolveAdditionalPropertiesForType(TypeScope scope) {
        return schemaGeneratorConfig.resolveAdditionalPropertiesForType(scope);
    }

    public Map<String, Type> resolvePatternProperties(FieldScope field) {
        return schemaGeneratorConfig.resolvePatternProperties(field);
    }

    public Map<String, Type> resolvePatternProperties(MethodScope method) {
        return schemaGeneratorConfig.resolvePatternProperties(method);
    }

    public Map<String, Type> resolvePatternPropertiesForType(TypeScope scope) {
        return schemaGeneratorConfig.resolvePatternPropertiesForType(scope);
    }

    public Integer resolveStringMinLength(FieldScope field) {
        return schemaGeneratorConfig.resolveStringMinLength(field);
    }

    public Integer resolveStringMinLength(MethodScope method) {
        return schemaGeneratorConfig.resolveStringMinLength(method);
    }

    public Integer resolveStringMinLengthForType(TypeScope scope) {
        return schemaGeneratorConfig.resolveStringMinLengthForType(scope);
    }

    public Integer resolveStringMaxLength(FieldScope field) {
        return schemaGeneratorConfig.resolveStringMaxLength(field);
    }

    public Integer resolveStringMaxLength(MethodScope method) {
        return schemaGeneratorConfig.resolveStringMaxLength(method);
    }

    public Integer resolveStringMaxLengthForType(TypeScope scope) {
        return schemaGeneratorConfig.resolveStringMaxLengthForType(scope);
    }

    public String resolveStringFormat(FieldScope field) {
        return schemaGeneratorConfig.resolveStringFormat(field);
    }

    public String resolveStringFormat(MethodScope method) {
        return schemaGeneratorConfig.resolveStringFormat(method);
    }

    public String resolveStringFormatForType(TypeScope scope) {
        return schemaGeneratorConfig.resolveStringFormatForType(scope);
    }

    public String resolveStringPattern(FieldScope field) {
        return schemaGeneratorConfig.resolveStringPattern(field);
    }

    public String resolveStringPattern(MethodScope method) {
        return schemaGeneratorConfig.resolveStringPattern(method);
    }

    public String resolveStringPatternForType(TypeScope scope) {
        return schemaGeneratorConfig.resolveStringPatternForType(scope);
    }

    public BigDecimal resolveNumberInclusiveMinimum(FieldScope field) {
        return schemaGeneratorConfig.resolveNumberInclusiveMinimum(field);
    }

    public BigDecimal resolveNumberInclusiveMinimum(MethodScope method) {
        return schemaGeneratorConfig.resolveNumberInclusiveMinimum(method);
    }

    public BigDecimal resolveNumberInclusiveMinimumForType(TypeScope scope) {
        return schemaGeneratorConfig.resolveNumberInclusiveMinimumForType(scope);
    }

    public BigDecimal resolveNumberExclusiveMinimum(FieldScope field) {
        return schemaGeneratorConfig.resolveNumberExclusiveMinimum(field);
    }

    public BigDecimal resolveNumberExclusiveMinimum(MethodScope method) {
        return schemaGeneratorConfig.resolveNumberExclusiveMinimum(method);
    }

    public BigDecimal resolveNumberExclusiveMinimumForType(TypeScope scope) {
        return schemaGeneratorConfig.resolveNumberExclusiveMinimumForType(scope);
    }

    public BigDecimal resolveNumberInclusiveMaximum(FieldScope field) {
        return schemaGeneratorConfig.resolveNumberInclusiveMaximum(field);
    }

    public BigDecimal resolveNumberInclusiveMaximum(MethodScope method) {
        return schemaGeneratorConfig.resolveNumberInclusiveMaximum(method);
    }

    public BigDecimal resolveNumberInclusiveMaximumForType(TypeScope scope) {
        return schemaGeneratorConfig.resolveNumberInclusiveMaximumForType(scope);
    }

    public BigDecimal resolveNumberExclusiveMaximum(FieldScope field) {
        return schemaGeneratorConfig.resolveNumberExclusiveMaximum(field);
    }

    public BigDecimal resolveNumberExclusiveMaximum(MethodScope method) {
        return schemaGeneratorConfig.resolveNumberExclusiveMaximum(method);
    }

    public BigDecimal resolveNumberExclusiveMaximumForType(TypeScope scope) {
        return schemaGeneratorConfig.resolveNumberExclusiveMaximumForType(scope);
    }

    public BigDecimal resolveNumberMultipleOf(FieldScope field) {
        return schemaGeneratorConfig.resolveNumberMultipleOf(field);
    }

    public BigDecimal resolveNumberMultipleOf(MethodScope method) {
        return schemaGeneratorConfig.resolveNumberMultipleOf(method);
    }

    public BigDecimal resolveNumberMultipleOfForType(TypeScope scope) {
        return schemaGeneratorConfig.resolveNumberMultipleOfForType(scope);
    }

    public Integer resolveArrayMinItems(FieldScope field) {
        return schemaGeneratorConfig.resolveArrayMinItems(field);
    }

    public Integer resolveArrayMinItems(MethodScope method) {
        return schemaGeneratorConfig.resolveArrayMinItems(method);
    }

    public Integer resolveArrayMinItemsForType(TypeScope scope) {
        return schemaGeneratorConfig.resolveArrayMinItemsForType(scope);
    }

    public Integer resolveArrayMaxItems(FieldScope field) {
        return schemaGeneratorConfig.resolveArrayMaxItems(field);
    }

    public Integer resolveArrayMaxItems(MethodScope method) {
        return schemaGeneratorConfig.resolveArrayMaxItems(method);
    }

    public Integer resolveArrayMaxItemsForType(TypeScope scope) {
        return schemaGeneratorConfig.resolveArrayMaxItemsForType(scope);
    }

    public Boolean resolveArrayUniqueItems(FieldScope field) {
        return schemaGeneratorConfig.resolveArrayUniqueItems(field);
    }

    public Boolean resolveArrayUniqueItems(MethodScope method) {
        return schemaGeneratorConfig.resolveArrayUniqueItems(method);
    }

    public Boolean resolveArrayUniqueItemsForType(TypeScope scope) {
        return schemaGeneratorConfig.resolveArrayUniqueItemsForType(scope);
    }
}
