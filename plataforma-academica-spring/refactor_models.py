import glob
import os

path = 'c:/Users/PAULO/OneDrive/Documentos/PlataformaAcademica/ProjetoFinal/plataforma-academica-spring/src/main/java/com/plataforma_academica\plataforma/model/*.java'
files = glob.glob(path)

for f in files:
    with open(f, 'r', encoding='utf-8') as file:
        content = file.read()
    
    modified = False
    
    if 'private Long id;' in content:
        content = content.replace('private Long id;', 'private UUID id;')
        modified = True

    if 'private UUID id;' in content:
        if 'import java.util.UUID;' not in content:
            if 'import jakarta.persistence.*;' in content:
                content = content.replace('import jakarta.persistence.*;', 'import jakarta.persistence.*;\nimport java.util.UUID;')
            elif 'package com.plataforma_academica.plataforma.model;' in content:
                content = content.replace('package com.plataforma_academica.plataforma.model;', 'package com.plataforma_academica.plataforma.model;\n\nimport java.util.UUID;')
            modified = True
            
        if '@GeneratedValue(strategy = GenerationType.IDENTITY)' in content:
            content = content.replace(
                '@GeneratedValue(strategy = GenerationType.IDENTITY)',
                '@Column(columnDefinition = "uuid", updatable = false, nullable = false)'
            )
            modified = True

        if '@PrePersist' not in content and 'public void onCreate' not in content:
            rindex = content.rfind('}')
            if rindex != -1:
                pre_persist_code = '\n    @PrePersist\n    public void onCreate() {\n        if (id == null) {\n            id = UUID.randomUUID();\n        }\n    }\n'
                content = content[:rindex] + pre_persist_code + content[rindex:]
                modified = True

    if modified:
        with open(f, 'w', encoding='utf-8') as file:
            file.write(content)
        print(f"Refatorado Model: {os.path.basename(f)}")

print("Refatoração de models concluída com sucesso!")
