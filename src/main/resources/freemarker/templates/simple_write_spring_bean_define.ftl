    @Bean("${classSimpleNameCamelCase}")
    @Scope("singleton")
    public ${classCanonicalName} get${classSimpleNamePascalCase}() {
        return new ${classSimpleName}();
    }
