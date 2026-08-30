$files = Get-ChildItem "src/main/java/com/plataforma_academica/plataforma/service/*.java"
foreach ($file in $files) {
    $c = [System.IO.File]::ReadAllText($file.FullName, [System.Text.Encoding]::UTF8)
    if ($c -notmatch 'import java\.util\.UUID;') {
        $c = $c -replace 'package com\.plataforma_academica\.plataforma\.service;', "package com.plataforma_academica.plataforma.service;`n`nimport java.util.UUID;"
        [System.IO.File]::WriteAllText($file.FullName, $c, (New-Object System.Text.UTF8Encoding $false))
        Write-Output "Atualizado Service Interface: $($file.Name)"
    }
}
Write-Output "Concluído!"
