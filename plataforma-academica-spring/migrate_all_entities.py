import glob
import os

path = 'c:/Users/PAULO/OneDrive/Documentos/PlataformaAcademica/ProjetoFinal/plataforma-academica-spring/src/main/java/com/plataforma_academica/plataforma/model/*.java'
files = glob.glob(path)

for f in files:
    with open(f, 'r', encoding='utf-8') as file:
        content = file.read()
    
    modified = False
    
    # Se já tem private UUID id;, pula ou verifica imports/@PrePersist
    if 'private Long id;' in content:
        content = content.replace('private Long id;', 'private UUID id;')
        modified = True
    
    if 'private UUID id;' in content:
        # Garantir import java.util.UUID;
        if 'import java.util.UUID;' not in content:
            # Inserir após o pacote ou primeiro import
            if 'import jakarta.persistence.*;' in content:
                content = content.replace('import jakarta.persistence.*;', 'import jakarta.persistence.*;\nimport java.util.UUID;')
            elif 'package com.plataforma_academica.plataforma.model;' in content:
                content = content.replace('package com.plataforma_academica.plataforma.model;', 'package com.plataforma_academica.plataforma.model;\n\nimport java.util.UUID;')
            modified = True
            
        # Substituir @GeneratedValue(strategy = GenerationType.IDENTITY) por @Column columnDefinition se presente
        gen_id_pattern = '@GeneratedValue(strategy = GenerationType.IDENTITY)\n    private UUID id;'
        col_def = '@Column(columnDefinition = "uuid", updatable = false, nullable = false)\n    private UUID id;'
        if gen_id_pattern in content:
            content = content.replace(gen_id_pattern, col_def)
            modified = True
        else:
            # Tentar variações de formatação
            if '@GeneratedValue' in content and 'private UUID id;' in content:
                # Substituir bloco de ID se necessário
                pass

        # Adicionar @PrePersist se não existir
        if '@PrePersist' not in content and 'public void onCreate' not in content:
            # Inserir antes da última chave fechadora
            rindex = content.rfind('}')
            if rindex != -1:
                pre_persist_code = '''
    @PrePersist
    public void onCreate() {
        if (id == null) {
            id = UUID.randomUUID();
        }
    }
'''
                content = content[:rindex] + pre_persist_code + content[rindex:]
                modified = True

    if modified:
        with open(f, 'w', encoding='utf-8') as file:
            file.write(content)
        print(f"Migrado para UUID: {os.path.basename(f)}")

print("Migração de entidades concluída!")
