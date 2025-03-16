import os
import shutil

# Base directory containing the version directories
BASE_DIR = 'game-runner/src'

# Versions to exclude
EXCLUDE_VERSIONS = ['v1_12_2', 'v1_8_9', 'main', 'test']

# Base version to copy from
BASE_VERSION = 'v1_21_3'

# The import statement to update
IMPORT_STATEMENT_TEMPLATE = 'import net.labymod.{version}.client.gui.screen.VersionedScreenWrapper;'
PACKAGE_STATEMENT_TEMPLATE = 'package dev_janheist_saveguimouseposition.{version};'

def copy_and_update_files():
    base_file_path = os.path.join(BASE_DIR, BASE_VERSION, 'java', f'dev_janheist_saveguimouseposition/{BASE_VERSION}', 'VersionedContainerController.java')

    for version_dir in os.listdir(BASE_DIR):
        if version_dir in EXCLUDE_VERSIONS or version_dir == BASE_VERSION or not os.path.isdir(os.path.join(BASE_DIR, version_dir)):
            continue

        import_statement = IMPORT_STATEMENT_TEMPLATE.format(version=version_dir)
        package_statement = PACKAGE_STATEMENT_TEMPLATE.format(version=version_dir)
        dest_dir = os.path.join(BASE_DIR, version_dir, 'java', f'dev_janheist_saveguimouseposition/{version_dir}')
        dest_file_path = os.path.join(dest_dir, 'VersionedContainerController.java')

        # Copy the file
        os.makedirs(dest_dir, exist_ok=True)
        shutil.copy(base_file_path, dest_file_path)
        print(f'Copied to {dest_file_path}')

        # Update the import statement
        with open(dest_file_path, 'r') as f:
            content = f.read()

        new_content = content.replace(
            f'import net.labymod.{BASE_VERSION}.client.gui.screen.VersionedScreenWrapper;',
            import_statement
        )

        new_content = new_content.replace(
            f'package dev_janheist_saveguimouseposition.{BASE_VERSION};',
            package_statement
        )

        with open(dest_file_path, 'w') as f:
            f.write(new_content)
        print(f'Updated import and package in {dest_file_path}')

if __name__ == '__main__':
    copy_and_update_files()
