$files = Get-ChildItem "src/main/java/com/plataforma_academica/plataforma/model/*.java"
foreach ($file in $files) {
    $c = [System.IO.File]::ReadAllText($file.FullName, [System.Text.Encoding]::UTF8)
    if ($c -match 'private Long id;') {
        $c = $c.Replace('private Long id;', 'private UUID id;')
        if ($c -notmatch 'import java\.util\.UUID;') {
            $c = $c -replace 'package com\.plataforma_academica\.plataforma\.model;', "package com.plataforma_academica.plataforma.model;`n`nimport java.util.UUID;"
        }
        if ($c -match '@GeneratedValue\(strategy = GenerationType\.IDENTITY\)') {
            $c = $c -replace '@GeneratedValue\(strategy = GenerationType\.IDENTITY\)', '@Column(columnDefinition = "uuid", updatable = false, nullable = false)'
        }
        if ($c -notmatch '@PrePersist') {
            $last = $c.LastIndexOf('}')
            if ($last -ge 0) {
                $prePersist = "`n`n    @PrePersist`n    public void onCreate() {`n        if (id == null) {`n            id = UUID.randomUUID();`n        }`n    }`n"
                $c = $c.Substring(0, $last) + $prePersist + $c.Substring($last)
            }
        }
        [System.IO.File]::WriteAllText($file.FullName, $c, (New-Object System.Text.UTF8Encoding $false))
        Write-Output "Migrado Model: $($file.Name)"
    }
}
Write-Output "Concluído!"
